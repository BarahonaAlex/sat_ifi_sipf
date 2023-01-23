/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.controllers.ContribuyenteController;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosComprasParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosReporteDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosVentasParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ConvenioPagosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DetalleConvenioPagoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DuasDucasReponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.dtos.ImportacionExportacionSiveDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ImportacionExportacionSivepaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.LineasDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParamsDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParamsLineasDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SipfCatDatoDto;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoRepository;
import gt.gob.sat.sat_ifi_sipf.utils.CellProperty;
import static gt.gob.sat.sat_ifi_sipf.utils.DateUtil.formatDate;
import gt.gob.sat.sat_ifi_sipf.utils.FileUtils;
import static gt.gob.sat.sat_ifi_sipf.utils.ManejoTexto.getSafe;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 10/06/2022
 * @version 1.0
 */
@Service
@Slf4j
public class ContribuyenteService {

    @Autowired
    private ConsumosService consumosService;

    @Autowired
    private CatDatoRepository catDatoRepository;

    /**
     * Metodo para obtener las informacion general del contribuyente
     *
     * @param NIT número de identificación tributaria
     * @since 10/06/2022
     * @return
     */
    public String getGeneralTaxpayerInformation(String NIT) {
        try {
            String ruta = "sat_rtu/contribuyentes/datos/general/".concat(NIT);
            return consumosService.consume(null, ruta, String.class, HttpMethod.GET);
        } catch (RestClientException e) {
            throw BusinessException.notFound("No se encontró información del NIT ingresado.");
        }
    }

    /**
     * Metodo para obtener las informacion del tipo de contribuyente
     * Empresa/Negocio
     *
     * @param NIT número de identificación tributaria (Empresa/Negocio)
     * @since 10/06/2022
     * @return
     */
    public String getTypeLegalService(String NIT) {
        try {
            String ruta = "sat_rtu/detalleTipoServicio/nitJuridico/".concat(NIT);
            return consumosService.consume(null, ruta, String.class, HttpMethod.GET);
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /**
     * Metodo para consultar vehiculos mediante un parametro nit
     *
     * @author Gabriel Ruano (agaruanom)
     * @since 28/07/2022
     * @param nit número de identificación tributaria (Empresa/Negocio)
     * @return getData
     */
    public List<Object> getVehiclesConsultation(String nit) {
        try {
            GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(nit, "vehiculos/" + nit, GeneralResponseDto.class, HttpMethod.GET);
            return resultado.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    ///////////////////////
    /**
     * Metodo que obtiene la informacion de importaciones del sistema SIVEPA
     *
     * @author Luis Villagran (lfvillag)
     * @param importacionSivepaDto
     * @since 21/07/2022
     * @return
     */
    public List<Object> getImportacionSivepa(ImportacionExportacionSivepaDto importacionSivepaDto) {
        try {
            GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(importacionSivepaDto, "importacion/exportacion", GeneralResponseDto.class, HttpMethod.POST);
            return resultado.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    ///////////////////////

    ///////////////////////
    /**
     * Metodo que obtiene la informacion de importaciones del sistema SIVEPA
     *
     * @author Luis Villagran (lfvillag)
     * @param importacionSivepaDto
     * @since 21/07/2022
     * @return
     */
    public List<Object> getImportacionSivepaDetalle(ImportacionExportacionSiveDetalleDto importacionSivepaDto) {
        try {
            GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(importacionSivepaDto, "importacion/exportacion/detalle", GeneralResponseDto.class, HttpMethod.POST);
            return resultado.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    ///////////////////////

    /**
     * Metodo para obtener convenios de pago del contribuyente
     *
     * @param NIT número de identificación tributaria (Empresa/Negocio)
     * @since 10/06/2022
     * @return
     */
    public List<ConvenioPagosDto> getPaymentAgreement(String pNit) {
        try {
            String ruta = "convenio/".concat(pNit);
            GeneralResponseDto<List<ConvenioPagosDto>> resultado = consumosService.consumeCompleteUrlOracle(null, ruta, GeneralResponseDto.class, HttpMethod.GET);
            return resultado.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /**
     * Metodo para obtener detalle convenios de pago del contribuyente
     *
     * @param NIT número de identificación tributaria
     * @since 10/06/2022
     * @return
     */
    public List<DetalleConvenioPagoDto> getPaymentAgreementDetail(String pNit, String pForm, String pDoc) {
        try {
            String ruta = "convenio/detalle/".concat(pNit).concat("/").concat(pForm).concat("/").concat(pDoc);
            GeneralResponseDto<List<DetalleConvenioPagoDto>> resultado = consumosService.consumeCompleteUrlOracle(null, ruta, GeneralResponseDto.class, HttpMethod.GET);
            return resultado.getData();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /**
     * Metodo para obtener la gerencia dell contribuyente
     *
     * @author Gabriel Ruano (agaruanom)
     * @param clasificacion, número de clasificacion
     * @param departamento, número de departamento
     * @since 10/08/2022
     * @return
     */
    public CatalogDataProjection getGerency(Integer clasificacion, Integer departamento) {

        if (clasificacion == 1371 || clasificacion == 1370) {
            return catDatoRepository.findByCodigoIngresado(clasificacion.toString());
        } else {
            SipfCatDatoDto result = consumosService.consume(null, "sat_catalogo/cat_dato/" + departamento, SipfCatDatoDto.class, HttpMethod.GET);
            //log.debug("hola" + result);
            return catDatoRepository.findByCodigoIngresado(new String().valueOf(result.getCodigoDatoPadre()));
        }
    }

    public ArrayList<AsisteLibrosReporteDto> getDetalleComprasDescarga(AsisteLibrosComprasParametrosDto dto) {
        try {
            String resultado = consumosService.consume(dto, "sat-ale-reportes/compras/decargaDetalles", String.class, HttpMethod.POST);
            resultado = resultado.replaceAll("\"", "");
            //ArrayList<AsisteLibrosReporteCompras> lista = new ArrayList<AsisteLibrosReporteCompras>();
            ArrayList<AsisteLibrosReporteDto> lista = new ArrayList<AsisteLibrosReporteDto>();
            String[] data = resultado.split("\n");
            for (int i = 0; i < data.length; i++) {
                String dataFila[] = data[i].split(",");
                lista.add(new AsisteLibrosReporteDto(dataFila[0], dataFila[1], dataFila[2], dataFila[3], dataFila[4], dataFila[5], dataFila[6], dataFila[7], dataFila[8], dataFila[9], dataFila[10], dataFila[11], dataFila[12], dataFila[13], dataFila[14], dataFila[15], dataFila[16], dataFila[17], dataFila[18], dataFila[19], dataFila[20], dataFila[21], dataFila[22], dataFila[23]));
            }
            return lista;

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public ArrayList<AsisteLibrosReporteDto> getDetalleVentasDescarga(AsisteLibrosVentasParametrosDto dto) {
        try {
            String resultado = consumosService.consume(dto, "sat-ale-reportes/decargaDetalles", String.class, HttpMethod.POST);
            resultado = resultado.replaceAll("\"", "");
            ArrayList<AsisteLibrosReporteDto> lista = new ArrayList<AsisteLibrosReporteDto>();
            String[] data = resultado.split("\n");
            for (int i = 0; i < data.length; i++) {
                String dataFila[] = data[i].split(",");
                lista.add(new AsisteLibrosReporteDto(dataFila[0], dataFila[1], dataFila[2], dataFila[3], dataFila[4], dataFila[5], dataFila[6], dataFila[7], dataFila[8], dataFila[9], dataFila[10], dataFila[11], dataFila[12], dataFila[13], dataFila[14], dataFila[15], dataFila[16], dataFila[17], dataFila[18], dataFila[19], dataFila[20], dataFila[21], dataFila[22], dataFila[23]));
            }
            return lista;
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public List<DuasDucasReponseDto> getDuasAndDucas(ParamsDuasDucasDto dto, HttpServletResponse response, Boolean export) {
        String filename = "N".equals(dto.getCodigo()) ? "Reporte DUA" : "Reporte DUCA";
        final Workbook book = FileUtils.getWorkbook("xlsx");
        final Sheet sheet = book.createSheet(filename);

        GeneralResponseDto<List<DuasDucasDto>> header = consumosService.consumeCompleteUrlOracle(
                dto, "/duas/ducas",
                new ParameterizedTypeReference<GeneralResponseDto<List<DuasDucasDto>>>() {
        },
                HttpMethod.POST
        );

        if (!export) {
            List<DuasDucasReponseDto> data = new ArrayList<>();
            header.getData().forEach(item -> {
                final ParamsLineasDuasDucasDto body = new ParamsLineasDuasDucasDto(
                        item.getPuertoEntrada(),
                        item.getAduanaEntrada(),
                        item.getAnio(),
                        item.getSecuencia().intValueExact()
                );
                GeneralResponseDto<List<LineasDuasDucasDto>> lines = consumosService.consumeCompleteUrlOracle(
                        body,
                        "/duas/ducas/detalle",
                        new ParameterizedTypeReference<GeneralResponseDto<List<LineasDuasDucasDto>>>() {
                },
                        HttpMethod.POST
                );
                lines.getData().forEach(line -> {
                    data.add(new DuasDucasReponseDto(item, line));
                });
            });
            return data;
        }

        header.getData().forEach(item -> {
            final ParamsLineasDuasDucasDto body = new ParamsLineasDuasDucasDto(
                    item.getPuertoEntrada(),
                    item.getAduanaEntrada(),
                    item.getAnio(),
                    item.getSecuencia().intValueExact()
            );

            GeneralResponseDto<List<LineasDuasDucasDto>> lines = consumosService.consumeCompleteUrlOracle(
                    body,
                    "/duas/ducas/detalle",
                    new ParameterizedTypeReference<GeneralResponseDto<List<LineasDuasDucasDto>>>() {
            },
                    HttpMethod.POST
            );
            lines.getData().forEach(line -> {
                Row row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getNumeroOrden()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(concatDUA(item)).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(formatDate(item.getFechaAceptacion())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getDocImpoExpo()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getRazonSocialImpoExpo()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getDocAgente()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getRazonSocialAgente()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getRegimenModalidad()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getClaseDeclaracion()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getPaisProceDestino()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getDepositoTemporal()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getCodDepositoFiscal()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getModoTransporte()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title("0").allBorders(true).build()); // Unica
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getTipoCambio())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getTotalValorAduanaMPI())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(lines.getData().size() + "").allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getFobDolares())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getFleteDolares())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getSeguroDolares())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getOtrosGastos())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // DAI TOTAL
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // IVA TOTAL
                FileUtils.createCell(sheet, row, CellProperty.builder().title(sheet.getPhysicalNumberOfRows() + "").allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getCodigoSAC()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Cuota/Contingente
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getNumeroBultos())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(item.getPesoNeto())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getPesoBrutoFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getPesoNetoFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getCantidadUnidadesFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Unidad de Medida
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getDescripMercaFR()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getPaisOrigen()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Region CP
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getAcuerdo1()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getAcuerdo2()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getValorAduanaFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Tasa impositiva
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // DAI LINEA
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // IVA LINEA
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getFobDolaresFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getFleteDolaresFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getSeguroDolaresFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getOtrosGastosFR())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Factura declarada
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Monto factura
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getProveedorDestinatario()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getFormaPago()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Otra Forma de pago
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getCantidadUnidadesDVA())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Unidad de medida DVA
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getIdComercialMercaDVA()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().allBorders(true).build()); // Caracteristicas de la mercaderia
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getMarcaDVA()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(line.getModeloDVA()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(getSafe(line.getValorUnitarioDVA())).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getIncoterm()).allBorders(true).build());
                FileUtils.createCell(sheet, row, CellProperty.builder().title(item.getSelectivo()).allBorders(true).build());
            });
        });

        createDuaHeader(sheet);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xlsx\"");
            book.write(response.getOutputStream());
            book.close();
            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(ContribuyenteController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void createDuaHeader(Sheet sheet) {
        XSSFColor blue = new XSSFColor(Color.decode("#9bc2e6"), null);
        XSSFColor green = new XSSFColor(Color.decode("#92d050"), null);
        FileUtils.createHeader(sheet, Arrays.asList(
                CellProperty.builder().title("1. Número de orden").color(blue).autoSizeColumn(true).build(),
                CellProperty.builder().title("2. Aduana entrada/salida").color(blue).build(),
                CellProperty.builder().title("3. Número de DUA ").color(blue).autoSizeColumn(true).build(),
                CellProperty.builder().title("4. Fecha de Aceptación").color(blue).build(),
                CellProperty.builder().title("6.2 No. Identificación").color(green).build(),
                CellProperty.builder().title("6.4 Razón Social").color(green).autoSizeColumn(true).build(),
                CellProperty.builder().title("7.2 No. Identificación (agente)").color(green).build(),
                CellProperty.builder().title("7.5 Razón Social (agente)").color(green).autoSizeColumn(true).build(),
                CellProperty.builder().title("8.1 Régimen y modalidad").color(blue).build(),
                CellProperty.builder().title("8.2. CLASE").color(blue).build(),
                CellProperty.builder().title("9 Pais de Procedencia / Destino").color(green).build(),
                CellProperty.builder().title("10. Deposito Temporal").color(green).build(),
                CellProperty.builder().title("11. Deposito Fiscal / Zona Franca").color(green).build(),
                CellProperty.builder().title("12. Modo (Transporte)").color(green).autoSizeColumn(true).build(),
                CellProperty.builder().title("UNICAS").color(new XSSFColor(Color.RED, null)).autoSizeColumn(true).build(),
                CellProperty.builder().title("15. Tipo Cambio (5 decimales)").color(green).bold(true).build(),
                CellProperty.builder().title("16.Total de Valor en Aduana MPI (Q.)").bold(true).color(green).build(),
                CellProperty.builder().title("18. Total No. De líneas").color(green).bold(true).build(),
                CellProperty.builder().title("21. Valor FOB (TOTAL $)").color(green).bold(true).build(),
                CellProperty.builder().title("22. Valor Flete (TOTAL $)").color(green).bold(true).build(),
                CellProperty.builder().title("23. Valor Seguro (TOTAL $)").color(green).bold(true).build(),
                CellProperty.builder().title("24. Otros Gastos (TOTAL $)").color(green).bold(true).build(),
                CellProperty.builder().title("25. DAI Q. (TOTAL)").color(green).bold(true).build(),
                CellProperty.builder().title("25. IVA Q. (TOTAL)").color(green).bold(true).build(),
                CellProperty.builder().title("31. Número de Línea").color(green).bold(true).build(),
                CellProperty.builder().title("32.1 Código SAC").color(blue).build(),
                CellProperty.builder().title("32.4 Cuota/Contingente").color(green).build(),
                CellProperty.builder().title("33.1 Número de Bultos").color(green).build(),
                CellProperty.builder().title("35. Peso Neto (kgs)").color(green).build(),
                CellProperty.builder().title("34. peso bruto de la mercancia").color(blue).build(),
                CellProperty.builder().title("35. peso neto de la mercancia").color(blue).build(),
                CellProperty.builder().title("36.1 Cantidad de Unidades").color(green).build(),
                CellProperty.builder().title("36.2 U. Med.").color(green).build(),
                CellProperty.builder().title("37. Descripcion de la mercancia").color(blue).width((short) 70).build(),
                CellProperty.builder().title("38. Pais de origen").color(blue).build(),
                CellProperty.builder().title("39. Region CP").color(green).build(),
                CellProperty.builder().title("40. Acuerdo 1").color(green).build(),
                CellProperty.builder().title("41. Acuerdo 2").color(green).build(),
                CellProperty.builder().title("42. Valor en Aduana MPI (valor CIF Q)").color(blue).build(),
                CellProperty.builder().title("43.3 Tasa Impositiva").color(green).build(),
                CellProperty.builder().title("43.4 DAI").color(green).build(),
                CellProperty.builder().title("43.4 IVA Q. ").color(green).bold(true).build(),
                CellProperty.builder().title("44. FOB $").color(blue).build(),
                CellProperty.builder().title("45. Valor Flete (por linea $)").color(green).bold(true).build(),
                CellProperty.builder().title("46. Valor Seguro (por linea $)").color(green).bold(true).build(),
                CellProperty.builder().title("47. Otros Gastos (por linea $)").color(green).bold(true).build(),
                CellProperty.builder().title("48.5 No factura declarada").color(blue).build(),
                CellProperty.builder().title("48.11 monto de factura").color(blue).build(),
                CellProperty.builder().title("Proveedor").color(blue).autoSizeColumn(true).build(),
                CellProperty.builder().title("FORMA DE PAGO DVA-FACTURA").color(blue).build(),
                CellProperty.builder().title("OTRA FORMA DE PAGO DVA-FACTURA").color(blue).build(),
                CellProperty.builder().title("CANTIDAD\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("UNIDAD DE MEDIDA\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("DESIGNACIÓN O IDENTIFICACIÓN COMERCIAL DE LAS MERCANCÍAS\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("CARACTERISTICAS DE LA MERCANCÍA\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("MARCA\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("MODELO Y/O ESTILO \n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("VALOR FOB Q UNITARIO\n(DVA)").color(new XSSFColor(Color.YELLOW, null)).fontSize((short) 8).centered(true).build(),
                CellProperty.builder().title("INCOTERMS").color(blue).autoSizeColumn(true).build(),
                CellProperty.builder().title("SELECTIVO").color(green).build()
        ).stream().map(item -> {
            item.setAllBorders(true);
            return item;
        }).collect(Collectors.toList()));

        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, sheet.getRow(0).getPhysicalNumberOfCells()));
        sheet.createFreezePane(3, 1);

    }

    private String concatDUA(DuasDucasDto data) {
        return data.getPuertoEntrada()
                .concat(data.getAduanaEntrada())
                .concat(data.getAnio())
                .concat(getSafe(data.getSecuencia()))
                .concat(getSafe(data.getVersion()));
    }
}
