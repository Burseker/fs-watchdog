package com.burseker.hiphub.fswatchdog.persistant.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "path_index", columnList = "path", unique = true),
        @Index(name = "md5_size_index", columnList = "md5, size")
    }
)
public class FileMetaIndex {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String md5;

    @Column(nullable = false)
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    private FileMetaIndex mainFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public FileMetaIndex getMainFile() {
        return mainFile;
    }

    public void setMainFile(FileMetaIndex mainFile) {
        this.mainFile = mainFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetaIndex that = (FileMetaIndex) o;
        return Objects.equals(id, that.id) && Objects.equals(path, that.path) && Objects.equals(md5, that.md5) && Objects.equals(size, that.size) && Objects.equals(mainFile, that.mainFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, md5, size, mainFile);
    }

    @Override
    public String toString() {
        return "FileMetaIndex{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", md5='" + md5 + '\'' +
                ", size=" + size +
                ", mainFile=" + mainFile.getId() +
                '}';
    }
}
