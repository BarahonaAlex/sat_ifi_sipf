/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.utils;

import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 *
 * @author crramosl
 */
@Slf4j
public class ReportesUtils {

    public static final String EXCEL = "xls";
    public static final String PDF = "pdf";
    public static final String WORD = "doc";

    /**
     * Metodo que recibe cualquier tipo de coleccion de datos y lo exporta en el
     * tipo de docuemnto solicitado utilizando JasperReports.
     *
     * @author crramosl
     *
     * @param data es la coleccion de datos a convertir en documento.
     * @param templateName es el nombre del formato de jasper report
     * (template.jrxml)
     * @param response es el objeto por el cual se enviara el docuemnto
     * generado.
     * @param type tipo de documento a exportar.
     * @param <T> Tipo de la información a plasmar en el reporte tiene que
     * extender de una colección de datos {@link Collection}
     */
    public static <T extends Collection> void crearReporte(T data, String templateName, HttpServletResponse response, String type) {
        try {
            InputStream jasperStream = ReportesUtils.class.getResourceAsStream("/".concat(templateName));
            JasperDesign design = JRXmlLoader.load(jasperStream);
            JasperReport report = JasperCompileManager.compileReport(design);
            Map<String, Object> parameterMap = new HashMap();
            JRDataSource jRDataSource = new JRBeanCollectionDataSource(data);
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment");
            parameterMap.put("datasource", jRDataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameterMap, jRDataSource);
            final OutputStream outputStream = response.getOutputStream();
            switch (type) {
                case PDF:
                    createPDFReport(jasperPrint, outputStream);
                    break;
                case EXCEL:
                    createExcelReport(jasperPrint, outputStream);
                    break;
                case WORD:
                    createWordReport(jasperPrint, outputStream);
            }
            if (type.equals(PDF)) {
                createPDFReport(jasperPrint, outputStream);
            } else {

            }
        } catch (JRException | IOException ex) {
            throw new ResourceNotFoundException("Error al generar archivo de reporte",
                    "Error: reporte" + ex,
                    500, "http://rtu.sat.gob.gt");
        }
    }

    /**
     * Metodo que genera las clausulas where de un query en base a un objeto
     * (object), las genera a un query base (baseQuery) y crea el query con el
     * administrador de entidades (entityManager).
     *
     * @author crramosl
     *
     * @param entityManager es el administrador de entidades utilizado para
     * crear el query.
     * @param baseQuery es el query base (antes de where) para la creación del
     * query.
     * @param object es la instancia del objeto que tiene la estructura de la
     * consulta.
     * @param type tipo del objeto que se retornará.
     * @return Query con los fitros calculados.
     *
     * @see ReporteEnlaceRegionDto ejemplo de esturctura valida de dto.
     */
    public static Query createCompiledQuery(final EntityManager entityManager, final String baseQuery, final Object object, final Class type) {
        final Method[] allMethods = object.getClass().getDeclaredMethods();
        final Query q = entityManager.createNativeQuery(createQuery(baseQuery, object), type);

        for (Method method : allMethods) {
            try {
                final Column column = method.getDeclaredAnnotation(Column.class);
                if (column != null) {
                    final Object value = method.invoke(object);
                    if (column.forceAdd() || (value != null && !value.equals("") && !value.equals(0) && !value.equals(0.0) && !value.equals(0.0f))) {
                        q.setParameter(method.getName(), value);
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ReportesUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return q;
    }

    private static void createExcelReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsExporter export = new JRXlsExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        export.setConfiguration(configuration);
        export.exportReport();
    }

    private static void createWordReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRDocxExporter export = new JRDocxExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        export.exportReport();
    }

    private static void createPDFReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRPdfExporter export = new JRPdfExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCreatingBatchModeBookmarks(false);
        export.setConfiguration(configuration);
        export.exportReport();
    }

    private static <T> String createQuery(final String baseQuery, final T object) {
        final StringBuilder query = new StringBuilder(baseQuery);
        final List<String> order = new ArrayList<>();
        final Method[] allMethods = object.getClass().getDeclaredMethods();
        boolean isFirst = true;

        for (final Method method : allMethods) {
            try {
                final Column column = method.getDeclaredAnnotation(Column.class);
                if (column != null) {
                    final Object value = method.invoke(object);

                    if (value != null && !value.equals("") && !value.equals(0) && !value.equals(0.0) && !value.equals(0.0f)) {
                        query.append(isFirst ? " where " : " and ")
                                .append(column.table())
                                .append(".")
                                .append(column.name())
                                .append(" ")
                                .append(column.operator())
                                .append(" :")
                                .append(method.getName());
                        isFirst = false;
                    }
                    if (column.orderBy()) {
                        order.add(column.table() + "." + column.name());
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ReportesUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        query.append(" order by ").append(order.stream().collect(Collectors.joining(" and ")));
        return query.toString();
    }

    @Target(value = {ElementType.METHOD, ElementType.FIELD})
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface Column {

        String name() default "";

        String table() default "";

        String operator() default "=";

        boolean forceAdd() default false;

        boolean orderBy() default false;
    }
}
