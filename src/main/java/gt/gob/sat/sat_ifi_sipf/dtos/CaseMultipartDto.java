/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.List;

/**
 *
 * @author rabaraho
 */
public class CaseMultipartDto {

    private CasosDto caseData;

    private List<putData> listFileData;

    /**
     * @return the caseData
     */
    public CasosDto getCaseData() {
        return caseData;
    }

    /**
     * @param caseData the caseData to set
     */
    public void setCaseData(CasosDto caseData) {
        this.caseData = caseData;
    }

    /**
     * @return the listFileData
     */
    public List<putData> getListFileData() {
        return listFileData;
    }

    /**
     * @param listFileData the listFileData to set
     */
    public void setListFileData(List<putData> listFileData) {
        this.listFileData = listFileData;
    }

    public class putData {

        private String idFile;
        private String fileName;
        private Integer index;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        /**
         * @return the idFile
         */
        public String getIdFile() {
            return idFile;
        }

        /**
         * @param idFile the idFile to set
         */
        public void setIdFile(String idFile) {
            this.idFile = idFile;
        }

        /**
         * @return the fileName
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * @param fileName the fileName to set
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
