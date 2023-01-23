package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeEntry {
    private Date createdAt;
    private boolean isFolder;
    private boolean isFile;
    private Date modifiedAt;
    private String name;
    private String id;
    private String idv;
    private List<String> aspectNames;
    private Map<String, Object> properties;
    
    /* Se agregan metodos geter y seter para variables booleanas ya que lombok
     * la escribe como isVariable y no puede mapearse 
     */
	public boolean isIsFolder() {
		return isFolder;
	}
	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	public boolean isIsFile() {
		return isFile;
	}
	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}
}