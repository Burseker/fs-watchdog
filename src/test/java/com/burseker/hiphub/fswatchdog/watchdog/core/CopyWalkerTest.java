package com.burseker.hiphub.fswatchdog.watchdog.core;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.watchdog.core.common.DeepMetaIndexCompare;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.burseker.hiphub.fswatchdog.utils.PrinterUtils.listToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO Добавить в тест контроль вызова события очереди, о том что файл отсутствует

@Slf4j
@DataJpaTest(showSql=false)
//@DataJpaTest(properties={
//        "spring.jpa.show-sql=false"
//})
//@TestPropertySource(properties = {
//        "spring.jpa.show-sql=false"
//})
class CopyWalkerTest {
    @Autowired
    FileMetaIndexRepository repository;

    CopyWalker walker;

    static class ActExp{
        public ActExp(FileMetaIndex index, @Nullable Integer copyKey) {
            this.index = index;
            this.copyKey = Optional.ofNullable(copyKey).map(Long::valueOf).orElse(null);
        }
        FileMetaIndex index;
        Long copyKey;
    }

    final DeepMetaIndexCompare comparator = new DeepMetaIndexCompare() {
        @Override
        public boolean compare(FileMetaIndex fileIndex1, FileMetaIndex fileIndex2) throws IOException{
            if(fileIndex1.getPath().toLowerCase().contains("null")) throw new IOException(fileIndex1.toString());
            if(fileIndex2.getPath().toLowerCase().contains("null")) throw new IOException(fileIndex2.toString());

            String[] arg1 = fileIndex1.getPath().split("_");
            String[] arg2 = fileIndex2.getPath().split("_");
            if( arg1.length != 2 || arg2.length != 2 ) return false;
            return arg1[1].equals(arg2[1]);
        }
    };

    @BeforeEach
    void setUp() {
        walker = new CopyWalker(repository, comparator);
    }

    @DisplayName("walkerMarkCopyKeysTest with different arguments")
    @ParameterizedTest
    @MethodSource({
            "sourceForWalkerMarkCopyKeysTest",
            "sourceForWalkerMarkCopyKeys_keyCorrections",
            "sourceForWalkerMarkCopyKeys_fileNotExist"
    })
    void walkerMarkCopyKeysTest(List<ActExp> actExpMetaIndexes) {

        log.info("--========= Input of walker.markCopyKeys() =========--");
        log.info(
            listToString(
                repository.saveAll(actExpMetaIndexes.stream().map(v->v.index).collect(Collectors.toList()))
            )
        );

        walker.markCopyKeys();

        Iterator<ActExp> it = actExpMetaIndexes.iterator();
        repository.findAll().forEach(v-> it.next().index=v);
        log.info("--========= Output actExpMetaIndexes =========--");
        printActualExpected(actExpMetaIndexes);

        actExpMetaIndexes.forEach(
                v-> assertEquals(v.copyKey, Optional.ofNullable(v.index.getCopyKey()).map(Long::valueOf).orElse(null), v.index.toString() + " under test")
        );
    }

    void printActualExpected(List<ActExp> actExp){
        log.info(
            listToString(
                actExp.stream().map(v -> v.index + " expected copyKey=" + v.copyKey).collect(Collectors.toList())
            )
        );
    }

    private static Stream<Arguments> sourceForWalkerMarkCopyKeysTest() {
        return Stream.of(
                Arguments.of(Named.of("все файлы относятся к одной группе",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("a_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("b_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("c_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("d_copy0").md5("hash_a").size(1L).build(), 0)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Часть файлов в группе, часть уникальные",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_2").size(1L).build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Две группы копий, остальные уникальные",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("04_copy4").md5("hash_4").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("05_copy4").md5("hash_4").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy4").md5("hash_4").size(1L).build(), 0)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("часть файлов относится к одной группе, часть к другой",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("01_copy1").md5("hash_b").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("04_copy1").md5("hash_b").size(1L).build(), 0)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("разные файлы с одинаковым хэшем и размером",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("a_copy0").md5("hash_a").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("b_copy1").md5("hash_a").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("c_copy2").md5("hash_a").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("d_copy3").md5("hash_a").size(1L).build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Две группы копий, есть файл с таким же хэшем, но не копия",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("04_copy4").md5("hash_4").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("05___###").md5("hash_4").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy4").md5("hash_4").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("07___###").md5("hash_4").size(1L).build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("разные файлы с разным хэшем",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("a_copy0").md5("hash_a").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("b_copy1").md5("hash_b").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("c_copy2").md5("hash_c").size(1L).build(), null),
                                        new ActExp(FileMetaIndex.builder().path("d_copy3").md5("hash_d").size(1L).build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("3 группы файлов с одинаковым хэшем",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("01_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_a").size(1L).build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03_copy1").md5("hash_a").size(1L).build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("04_copy2").md5("hash_a").size(1L).build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("05_copy2").md5("hash_a").size(1L).build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("06_copy1").md5("hash_a").size(1L).build(), 1)
                                )
                                .collect(Collectors.toList())
                ))
        );
    }


    private static Stream<Arguments> sourceForWalkerMarkCopyKeys_keyCorrections() {
        return Stream.of(
                Arguments.of(Named.of("Новый файл(02_copy0) добавляется в группу 1. Новый файл(04_copy4) добавляется в группу 2, исправляется группа файла 07_copy4",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L)             .build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("04_copy4").md5("hash_4").size(1L)             .build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("05_copy4").md5("hash_4").size(1L).copyKey("2").build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy4").md5("hash_4").size(1L).copyKey("1").build(), 2)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Новый файл(07_copy0) добавляется в группу 1, исправляется группа файлов 02_copy0, 04_copy0",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).copyKey("0").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("04_copy0").md5("hash_0").size(1L).copyKey("2").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("05___###").md5("hash_4").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy0").md5("hash_0").size(1L)             .build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("07___###").md5("hash_4").size(1L)             .build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Новый файл(01_copy1) добавляется в группу 0, группа 2(*_copy2) остается неизменной",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("01_copy1").md5("hash_a").size(1L)             .build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_2").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03_copy1").md5("hash_a").size(1L).copyKey("0").build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("04_copy2").md5("hash_a").size(1L).copyKey("2").build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("05_copy2").md5("hash_a").size(1L).copyKey("2").build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("06_copy1").md5("hash_a").size(1L).copyKey("1").build(), 0)
                                )
                                .collect(Collectors.toList())
                ))
        );
    }

    private static Stream<Arguments> sourceForWalkerMarkCopyKeys_fileNotExist() {
        return Stream.of(
                Arguments.of(Named.of("Файл не попадает на сравнение, поэтому остается незамеченным",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("NULL")    .md5("hash_1").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L)             .build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("04_copy4").md5("hash_4").size(1L)             .build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("05_copy4").md5("hash_4").size(1L).copyKey("2").build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy4").md5("hash_4").size(1L).copyKey("1").build(), 2)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Отсутствующий файл попал на сравнение, корректного файла копии без группы нет",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).copyKey("0").build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("04_copy0").md5("hash_0").size(1L).copyKey("2").build(), 2),
                                        new ActExp(FileMetaIndex.builder().path("05___###").md5("hash_4").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("NULL")    .md5("hash_0").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07___###").md5("hash_0").size(1L)             .build(), null)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Отсутствующий файл попал на сравнение, есть корректный файл копия без группы",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("00_copy0").md5("hash_0").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("01___###").md5("hash_1").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("02_copy0").md5("hash_0").size(1L).copyKey("0").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03___###").md5("hash_3").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("04_copy0").md5("hash_0").size(1L).copyKey("2").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("05___###").md5("hash_4").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("06___###").md5("hash_6").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("NULL")    .md5("hash_0").size(1L)             .build(), null),
                                        new ActExp(FileMetaIndex.builder().path("07_copy0").md5("hash_0").size(1L)             .build(), 1)
                                )
                                .collect(Collectors.toList())
                )),
                Arguments.of(Named.of("Новый файл(01_copy1) добавляется в группу 0, группа 2(*_copy2) остается неизменной",
                        Stream
                                .of(
                                        new ActExp(FileMetaIndex.builder().path("01_copy1").md5("hash_a").size(1L)             .build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("02__NULL").md5("hash_2").size(1L).copyKey("1").build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("03_copy1").md5("hash_a").size(1L)             .build(), 0),
                                        new ActExp(FileMetaIndex.builder().path("04_copy2").md5("hash_a").size(1L)             .build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("05_copy2").md5("hash_a").size(1L)             .build(), 1),
                                        new ActExp(FileMetaIndex.builder().path("01__NULL").md5("hash_a").size(1L)             .build(), null)
                                )
                                .collect(Collectors.toList())
                ))
        );
    }
}
