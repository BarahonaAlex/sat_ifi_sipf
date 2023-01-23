/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gt.gob.sat.sat_ifi_sipf.constants.Constants;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.IndicadorPlanAnualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.MesPlanAnualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PlanAnualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfIndicadorPlanAnual;
import gt.gob.sat.sat_ifi_sipf.models.SipfMetaPlanAnual;
import gt.gob.sat.sat_ifi_sipf.models.SipfPlanAnual;
import gt.gob.sat.sat_ifi_sipf.projections.PlanAnualProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.IndicadorPlanAnualRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.MetaPlanAnualRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.PlanAnualRepository;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author crramosl
 */
@Transactional
@Service
@Slf4j
public class PlanAnualService {

    @Autowired
    PlanAnualRepository yearlyPlan;

    @Autowired
    IndicadorPlanAnualRepository yearlyPlanIndicator;

    @Autowired
    MetaPlanAnualRepository yearlyPlanGoal;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    @Transactional(rollbackFor = Exception.class)
    public Integer createYearlyPlan(final PlanAnualDto body, final UserLogged user) {
        if (yearlyPlan.findByValor(body.getYear()).isPresent()) {
            throw BusinessException.unprocessableEntity(Message.YEARLY_PLAN_ALREADY_CREATED.getText(body.getYear()));
        }

        final SipfPlanAnual plan = yearlyPlan.save(SipfPlanAnual.builder()
                .valor(body.getYear())
                .usuario(user.getLogin())
                .ip(user.getIp())
                .fecha(new Date())
                .build()
        );

        final List<SipfIndicadorPlanAnual> indicators = body.getIndicators()
                .stream()
                .map(indicator
                        -> SipfIndicadorPlanAnual.builder()
                        .idPlan(plan.getIdPlan())
                        .idIndicador(indicator.getId())
                        .valor(indicator.getValue())
                        .usuario(user.getLogin())
                        .ip(user.getIp())
                        .fecha(new Date())
                        .build()
                ).collect(Collectors.toList());

        yearlyPlanIndicator.saveAll(indicators);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_plan_anual")
                        .idCambioRegistro(String.valueOf(plan.getIdPlan()))
                        .idTipoOperacion(404)
                        .data(new Gson().toJson(plan))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );

        indicators.forEach(indicator -> {
            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_indicador_plan_anual")
                            .idCambioRegistro(String.valueOf(indicator.getIdIndicador()))
                            .idTipoOperacion(404)
                            .data(new Gson().toJson(indicator))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });

        return plan.getIdPlan();
    }

    @Transactional(rollbackFor = Exception.class)
    public void createMonth(final MesPlanAnualDto body, final UserLogged user) {
        if (yearlyPlanGoal.existsByIdPlanAndMesAndIdTipoMeta(body.getPlan(), body.getMonth(), body.getType())) {
            throw BusinessException.unprocessableEntity(Message.MONTH_ALREADY_CREATED.getText(Constants.MONTHS_OF_YEAR[body.getMonth() - 1]));
        }
        final List<SipfMetaPlanAnual> goals = body.getGoals().stream()
                .map(goal -> {
                    return SipfMetaPlanAnual.builder()
                            .idPlan(body.getPlan())
                            .mes(body.getMonth())
                            .idTipoMeta(body.getType())
                            .idGerencia(goal.getManagement())
                            .idDepartamento(goal.getDepartament())
                            .valor(goal.getValue())
                            .usuario(user.getLogin())
                            .ip(user.getIp())
                            .fecha(new Date())
                            .build();
                }
                ).collect(Collectors.toList());

        yearlyPlanGoal.saveAll(goals);

        goals.forEach(goal -> {
            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_meta_plan_anual")
                            .idCambioRegistro(String.valueOf(goal.getIdMeta()))
                            .idTipoOperacion(404)
                            .data(new Gson().toJson(goal))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateYearlyPlan(Integer year, PlanAnualDto body, UserLogged user) {
        if (yearlyPlan.findByValor(body.getYear()).isPresent() && !year.equals(body.getYear())) {
            throw BusinessException.unprocessableEntity(Message.YEARLY_PLAN_ALREADY_UPDATED.getText(body.getYear()));
        }

        final SipfPlanAnual plan = yearlyPlan.findByValor(year).orElseThrow(()
                -> BusinessException.notFound(Message.NO_YEALY_PLAN_EXISTS.getText(year))
        );

        final List<SipfIndicadorPlanAnual> indicators = yearlyPlanIndicator.findByIdPlan(plan.getIdPlan());

        final List<SipfIndicadorPlanAnual> deleted = indicators.stream()
                .filter((t) -> !body.getIndicators().stream().map(IndicadorPlanAnualDto::getId).collect(toSet()).contains(t.getIdIndicador())).collect(Collectors.toList());
        final List<SipfIndicadorPlanAnual> old = indicators.stream()
                .filter((t) -> body.getIndicators().stream().map(IndicadorPlanAnualDto::getId).collect(toSet()).contains(t.getIdIndicador()))
                .collect(Collectors.toList());

        final JsonObject data = new JsonObject();
        data.add("valorAnterior", new Gson().toJsonTree(plan));

        plan.setValor(body.getYear());
        plan.setUsuario(user.getLogin());
        plan.setIp(user.getIp());
        plan.setFecha(new Date());

        deleted.forEach(indicator -> {

            yearlyPlanIndicator.delete(indicator);

            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_indicador_plan_anual")
                            .idCambioRegistro(String.valueOf(indicator.getIdIndicador()))
                            .idTipoOperacion(406)
                            .data(new Gson().toJson(indicator))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });

        body.getIndicators()
                .stream()
                .filter(item -> !deleted.stream().map(SipfIndicadorPlanAnual::getIdIndicador).collect(toSet()).contains(item.getId()))
                .forEach(indicator -> {
                    final JsonObject dataIndicator = new JsonObject();
                    List<SipfIndicadorPlanAnual> oldMember = old.stream().filter(item -> item.getIdIndicador().equals(indicator.getId())).collect(Collectors.toList());
                    SipfIndicadorPlanAnual newIndicator = SipfIndicadorPlanAnual.builder()
                            .idPlan(plan.getIdPlan())
                            .idIndicador(indicator.getId())
                            .valor(indicator.getValue())
                            .usuario(user.getLogin())
                            .ip(user.getIp())
                            .fecha(new Date())
                            .build();

                    if (oldMember.size() > 0) {
                        newIndicator = oldMember.get(0);
                        dataIndicator.add("valorAnterior", new Gson().toJsonTree(newIndicator));
                    }

                    newIndicator.setValor(indicator.getValue());
                    newIndicator.setUsuario(user.getLogin());
                    newIndicator.setIp(user.getIp());
                    newIndicator.setFecha(new Date());

                    yearlyPlanIndicator.save(newIndicator);

                    dataIndicator.add("valorNuevo", new Gson().toJsonTree(newIndicator));

                    historialOperacionesRepository.save(
                            SipfHistoricoOperaciones.builder()
                                    .nombreTabla("sipf_indicador_plan_anual")
                                    .idCambioRegistro(String.valueOf(indicator.getId()))
                                    .idTipoOperacion(oldMember.size() > 0 ? 405 : 404)
                                    .data(new Gson().toJson(dataIndicator))
                                    .fechaModifica(new Date())
                                    .usuarioModifica(user.getLogin())
                                    .ipModifica(user.getIp())
                                    .build()
                    );
                });

        data.add("valorNuevo", new Gson().toJsonTree(plan));

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_plan_anual")
                        .idCambioRegistro(String.valueOf(plan.getIdPlan()))
                        .idTipoOperacion(405)
                        .data(new Gson().toJson(data))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMonth(Integer id, MesPlanAnualDto body, UserLogged user) {
        final List<SipfMetaPlanAnual> goals = yearlyPlanGoal.findByIdPlanAndMesAndIdTipoMeta(body.getPlan(), body.getMonth(), body.getType());

        if (goals.isEmpty()) {
            throw BusinessException.notFound(Message.NO_MONTH_EXISTS.getText(Constants.MONTHS_OF_YEAR[body.getMonth() - 1]));
        }

        body.getGoals().forEach(newGoal -> {
            List<SipfMetaPlanAnual> goal = goals.stream().filter(oldGoal -> Objects.equals(oldGoal.getIdMeta(), newGoal.getId())).collect(Collectors.toList());
            if (goal.isEmpty()) {
                return;
            }

            goal.get(0).setValor(newGoal.getValue());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteYearlyPlan(Integer id, UserLogged user) {
        final SipfPlanAnual plan = yearlyPlan.findById(id).orElseThrow(()
                -> BusinessException.notFound(Message.NO_YEALY_PLAN_EXISTS_ID.getText())
        );
        yearlyPlanIndicator.findByIdPlan(id).forEach(indicator -> {
            yearlyPlanIndicator.delete(indicator);

            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_indicador_plan_anual")
                            .idCambioRegistro(String.valueOf(indicator.getIdIndicador()))
                            .idTipoOperacion(406)
                            .data(new Gson().toJson(indicator))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });

        yearlyPlanGoal.findByIdPlan(id).forEach(goal -> {
            yearlyPlanGoal.delete(goal);

            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_meta_plan_anual")
                            .idCambioRegistro(String.valueOf(goal.getIdMeta()))
                            .idTipoOperacion(406)
                            .data(new Gson().toJson(goal))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });

        yearlyPlan.delete(plan);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_plan_anual")
                        .idCambioRegistro(String.valueOf(plan.getIdPlan()))
                        .idTipoOperacion(406)
                        .data(new Gson().toJson(plan))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMonthYearlyPlan(Integer plan, Integer month, Integer type, UserLogged user) {
        yearlyPlan.findById(plan).orElseThrow(()
                -> BusinessException.notFound(Message.NO_YEALY_PLAN_EXISTS_ID.getText())
        );

        if (!yearlyPlanGoal.existsByIdPlanAndMesAndIdTipoMeta(plan, month, type)) {
            throw BusinessException.notFound(Message.NO_MONTH_EXISTS.getText(Constants.MONTHS_OF_YEAR[month - 1]));
        }

        yearlyPlanGoal.findByIdPlanAndMesAndIdTipoMeta(plan, month, type).forEach(goal -> {
            yearlyPlanGoal.delete(goal);

            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_meta_plan_anual")
                            .idCambioRegistro(String.valueOf(goal.getIdMeta()))
                            .idTipoOperacion(406)
                            .data(new Gson().toJson(goal))
                            .fechaModifica(new Date())
                            .usuarioModifica(user.getLogin())
                            .ipModifica(user.getIp())
                            .build()
            );
        });
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<PlanAnualProjection> getYearlyPlans() {
        return yearlyPlan.getAllPlans();
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PlanAnualProjection getYearlyPlan(Integer id) {
        return yearlyPlanGoal.getPlanById(id).orElseThrow(()
                -> BusinessException.notFound(Message.NO_YEALY_PLAN_EXISTS_ID.getText())
        );
    }
}
