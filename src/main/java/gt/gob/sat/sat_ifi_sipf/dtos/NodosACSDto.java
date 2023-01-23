/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ruarcuse
 */
public class NodosACSDto {

    private Date createdAt;
    private String id;
    private String idv;
    private boolean isFile;
    private boolean isFolder;
    private Date modifiedAt;
    private String name;
    private Map<String, Object> properties;
    private List<String> aspectNames;

    public NodosACSDto() {
    }

    public NodosACSDto(Date createdAt, String id, String idv, boolean isFile, boolean isFolder, Date modifiedAt, String name, Map<String, Object> properties, List<String> aspectNames) {
        this.createdAt = createdAt;
        this.id = id;
        this.idv = idv;
        this.isFile = isFile;
        this.isFolder = isFolder;
        this.modifiedAt = modifiedAt;
        this.name = name;
        this.properties = properties;
        this.aspectNames = aspectNames;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdv() {
        return idv;
    }

    public void setIdv(String idv) {
        this.idv = idv;
    }

    public boolean isIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public boolean isIsFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<String> getAspectNames() {
        return aspectNames;
    }

    public void setAspectNames(List<String> aspectNames) {
        this.aspectNames = aspectNames;
    }

    @Override
    public String toString() {
        return "NodosACS{" + "createAt=" + createdAt + ", id=" + id + ", idv=" + idv + ", isFile=" + isFile + ", isFolder=" + isFolder + ", modifiedAt=" + modifiedAt + ", name=" + name + ", properties=" + properties + ", aspectNames=" + aspectNames + '}';
    }
}
