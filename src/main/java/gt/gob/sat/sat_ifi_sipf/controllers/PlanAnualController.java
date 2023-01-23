/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.MesPlanAnualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PlanAnualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.PlanAnualProjection;
import gt.gob.sat.sat_ifi_sipf.services.PlanAnualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author crramosl
 */
@Api(tags = {"Plan Anual de Fiscalizacion"})
@Validated
@RestController
@Slf4j
@RequestMapping("/yearly/plan")
public class PlanAnualController {

    @Autowired
    private PlanAnualService yearlyPlan;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea el encabezado de un plan anual de fiscalizacion")
    public Integer createYearlyPlan(@RequestBody @Valid PlanAnualDto body, @ApiIgnore UserLogged user) {
        return yearlyPlan.createYearlyPlan(body, user);
    }

    @PostMapping(path = "/month", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea el detalle de un mes para el plan anual de fiscalizacion")
    public void createMonth(@RequestBody @Valid MesPlanAnualDto body, @ApiIgnore UserLogged user) {
        yearlyPlan.createMonth(body, user);
    }

    @PutMapping(path = "/{year}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Modifica el encabezado de un plan anual de fiscalizacion")
    public void updateYearlyPlan(@PathVariable Integer year, @RequestBody @Valid PlanAnualDto body, @ApiIgnore UserLogged user) {
        yearlyPlan.updateYearlyPlan(year, body, user);
    }

    @PutMapping(path = "/month/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Modifica el detalle de un mes para el plan anual de fiscalizacion")
    public void updateMonth(@PathVariable Integer id, @RequestBody @Valid MesPlanAnualDto body, @ApiIgnore UserLogged user) {
        yearlyPlan.updateMonth(id, body, user);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna un listado de planes anual de fiscalizacion, con informacíon general")
    public List<PlanAnualProjection> getYearlyPlans() {
        return yearlyPlan.getYearlyPlans();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna el detalle de un plan anual de fiscalizacion")
    public PlanAnualProjection getYearlyPlan(@PathVariable Integer id) {
        return yearlyPlan.getYearlyPlan(id);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Elimina el encabezado y el detalle de un plan anual de fiscalizacion")
    public void deleteYearlyPlan(@PathVariable Integer id, @ApiIgnore UserLogged user) {
        yearlyPlan.deleteYearlyPlan(id, user);
    }

    @DeleteMapping(path = "/month/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna el detalle de un plan anual de fiscalizacion")
    public void deleteYearlyPlan(@PathVariable Integer id, @RequestParam Integer month, @RequestParam Integer type, @ApiIgnore UserLogged user) {
        yearlyPlan.deleteMonthYearlyPlan(id, month, type, user);
    }
}
