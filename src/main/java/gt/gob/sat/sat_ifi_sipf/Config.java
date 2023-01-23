package gt.gob.sat.sat_ifi_sipf;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.components.UserInterceptor;
import gt.gob.sat.sat_ifi_sipf.components.UserLocator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Data
@Slf4j
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class Config implements WebMvcConfigurer {

    @Autowired
    Detector detector;
    /**
     * Interceptor que verifica si el usuario esta autenticado.
     */
    @Autowired
    private UserInterceptor userInterceptor;

    /**
     * Localizador del bean con los datos del usuario que esta autenticado.
     */
    @Autowired
    private UserLocator userLocator;

    @Value("${sat.consumos.pingUrlInterna}")
    private String pingUrlInterna;
    @Value("${sat.config.siteACS}")
    private String siteACS;
    @Value("${sat.config.secretKeyImages}")
    private String secretKeyImages;
    @Value("${sat.config.secretKeyProcessInstances}")
    private String secretKeyProcessInstances;
    @Value("${sat.config.acs-secret-key}")
    private String acsSecretKey;
    @Value("${sat.config.idPFIMasiva}")
    private String idPFIMasiva;
    @Value("${sat.config.idProcesoInsumo}")
    private String idProcesoInsumo;
    @Value("${sat.config.idProcesoCaso}")
    private String idProcesoCaso;
    @Value("${sat.config.idProcesoProgramasFiscales}")
    private String idProcesoProgramasFiscales;
    @Value("${sat.config.testing}")
    private Boolean testing;
    @Value("${sat.config.usrWebServiceOracle}")
    private String usrWebServiceOracle;
    @Value("${sat.config.passWebServiceOracle}")
    private String passWebServiceOracle;
    @Value("${sat.consumos.pingUrlWsOracle}")
    private String pingUrlWsOracle;
    @Value("${sat.consumos.pingUrlWsSqlServer}")
    private String pingUrlWsSqlServer;
    @Value("${sat.config.usrWebServiceProsis}")
    private String usrWebServiceProsis;
    @Value("${sat.config.passWebServiceProsis}")
    private String passWebServiceProsis;

    @Value("${sat.file.bucket.name}")
    private String bucketName;

    /**
     * Metodo que permite agregar interceptores a los controladores MVC de
     * spring.
     *
     * @param registroInterceptores Registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registroInterceptores) {
        String[] paths = {"/scope/massive/**", "/cases/**", "/catalog/**",
            "/collaborators/**", "/content/**", "/workgroups/**", "/input/**",
            "/findings/**", "/fiscal/program/**", "/work/positions/**",
            "/reports/**", "/administrative/units/**", "/users/**", "/perfil/**",
            "/yearly/plan/**", "/tax/payer/**", "/fiscal/credit/**", "/complaint/**", 
            "/system/params/**", "/template/documents/**", "/tax/presences/**"
        };

        registroInterceptores
                .addInterceptor(this.userInterceptor)
                .addPathPatterns(paths);
    }

    /**
     * Metodo que permite agregar resolvedores de parametros para los metodos de
     * los controladores MVC de spring.
     *
     * @param localizadores Listado de localizadores de parametros
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> localizadores) {

        localizadores.add(this.userLocator);
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1")
                .build();
    }
}
