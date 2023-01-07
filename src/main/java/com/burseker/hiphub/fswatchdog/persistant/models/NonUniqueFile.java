package com.burseker.hiphub.fswatchdog.persistant.models;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Builder
public class NonUniqueFile {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String md5;

    @Column(nullable = false)
    private Long size;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonUniqueFile that = (NonUniqueFile) o;
        return Objects.equals(id, that.id) && Objects.equals(path, that.path) && Objects.equals(md5, that.md5) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, md5, size);
    }

    @Override
    public String toString() {
        return "NonUniqueFile{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", md5='" + md5 + '\'' +
                ", size=" + size +
                '}';
    }
}
