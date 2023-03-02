package com.burseker.hiphub.fswatchdog.service;

import com.burseker.hiphub.fswatchdog.persistant.daos.FileMetaIndexRepository;
import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import com.burseker.hiphub.fswatchdog.utils.FileDeepCompare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Slf4j
@Service
public class FileCopyWalker {
    private static final String copyKeyNull = null;

    private final FileMetaIndexRepository repository;

    private final DeepMetaIndexCompare comparator;

    public FileCopyWalker(@Autowired FileMetaIndexRepository repository, @Autowired DeepMetaIndexCompare comparator) {
        this.repository = repository;
        this.comparator = comparator;
    }

    public void markRepeating(){
        log.info("markRepeating(). entry point");

        Iterable<FileMetaIndex> res = repository.findAll();
        Map<Long, FileMetaIndex> corrected = new HashMap<>();
        res.forEach(
            val -> {

                if(!corrected.containsKey(val.getId())){
                    log.info("Element under test: {}", val);
                    Iterable<FileMetaIndex> same = repository.findByMd5AndSize(val.getMd5(), val.getSize());
                    same.forEach(
                        val2 -> {
                            if(!Objects.equals(val.getId(), val2.getId())) {
                                if(deepMetaIndexCompare(val, val2)) {
                                    log.info("    - same element: {}", val2);
                                    val2.setMainFile(val);
                                    corrected.put(val2.getId(), val2);
                                }
                            }
                        }
                    );
                } else {
                    log.info("Element under test: {} is copy of entity id={}", val, corrected.get(val.getId()).getMainFile().getId());
                }
            }
        );

        repository.saveAll(corrected.values());
    }

    public void markCopyKeys(){
        log.info("markCopyKeys(). entry point");
        Iterable<FileMetaIndex> res = repository.findAll();

        Map<Long, FileMetaIndex> corrected = new HashMap<>();
        res.forEach(
            val -> {
                log.info("Element under test: {}", val);
                if( corrected.containsKey(val.getId()) ) return;

                if(val.getCopyKey() != null) return;

                Iterable<FileMetaIndex> same = repository.findByMd5AndSize(val.getMd5(), val.getSize());
                Iterator<FileMetaIndex> iterator = same.iterator();
                if(iterator.hasNext())
                    if(iterator.next().getId().equals(val.getId()) && !iterator.hasNext()) return;

                String copyKeyCandidate = null;
                List<FileMetaIndex> copies = new ArrayList<>();
                Set<String> copyKeyToIgnore = new HashSet<>();

                for( FileMetaIndex sameVal : same){
                    log.debug("  - sameVal: {}", sameVal);
                    if(copyKeyToIgnore.contains(sameVal.getCopyKey())) continue;

                    if(deepMetaIndexCompare(val, sameVal)) {
                        if(sameVal.getCopyKey() != null) {
                            if(copyKeyCandidate==null){
                                copyKeyCandidate=sameVal.getCopyKey();
                                copyKeyToIgnore.add(copyKeyCandidate);
                            } else if(!copyKeyCandidate.equals(sameVal.getCopyKey())){
                                sameVal.setCopyKey(null);
                            }
                        }

                        copies.add(sameVal);
                        log.debug("    ++ sameVal.id={} add to copies", sameVal.getId());
                    } else {
                        Optional.ofNullable(sameVal.getCopyKey()).ifPresent(copyKeyToIgnore::add);
                    }
                }
                if(copies.size()==1) return;

                final String copyKey = calculateCopyKey(copyKeyCandidate, copyKeyToIgnore);
                copies.forEach(v->{
                    if(v.getCopyKey() == null){
                        v.setCopyKey(copyKey);
                        corrected.put(v.getId(), v);
                    }
                });
            }
        );

        if(!corrected.isEmpty()) repository.saveAll(corrected.values());
    }

    public boolean deepMetaIndexCompare(FileMetaIndex fileIndex1, FileMetaIndex fileIndex2) {
        try{
            return comparator.compare(fileIndex1, fileIndex2);
        } catch (IOException | RuntimeException e){
            log.warn("deepMetaIndexCompare throws exception: ", e);
            log.warn("notify check file {}", fileIndex1);
            log.warn("notify check file {}", fileIndex2);
        }
        return false;
    }
    private String calculateCopyKey(String candidate, Set<String> ignored){
        if( candidate!= null ) return candidate;
        Long index = 0L;
        while(true){
            if( !ignored.contains(index.toString()) ) return index.toString();
            index=index+1;
        }
    }
}
