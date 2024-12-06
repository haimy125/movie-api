package com.movie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * Configuration class for customizing Spring MVC settings, including enabling CORS
 * (Cross-Origin Resource Sharing) for specified origins and methods.
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS settings to allow cross-origin requests.
     * This method enables cross-origin requests for all endpoints, allowing
     * requests from `http://localhost:3000` with specified HTTP methods and headers.
     *
     * @param registry The {@link CorsRegistry} used to configure CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Đặt URL của frontend của bạn
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configures path matching options. No additional customization is applied here.
     *
     * @param configurer The {@link PathMatchConfigurer} for customizing path matching.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        WebMvcConfigurer.super.configurePathMatch(configurer);
    }

    /**
     * Configures content negotiation settings. No additional customization is applied here.
     *
     * @param configurer The {@link ContentNegotiationConfigurer} for configuring content negotiation.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        WebMvcConfigurer.super.configureContentNegotiation(configurer);
    }

    /**
     * Configures asynchronous request processing settings. No additional customization is applied here.
     *
     * @param configurer The {@link AsyncSupportConfigurer} for configuring async support.
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        WebMvcConfigurer.super.configureAsyncSupport(configurer);
    }

    /**
     * Configures default servlet handling. No additional customization is applied here.
     *
     * @param configurer The {@link DefaultServletHandlerConfigurer} for enabling default servlet handling.
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        WebMvcConfigurer.super.configureDefaultServletHandling(configurer);
    }

    /**
     * Adds formatters for data conversion. No additional formatters are added here.
     *
     * @param registry The {@link FormatterRegistry} for registering formatters.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
    }

    /**
     * Adds interceptors for pre- and post-processing of handler methods. No interceptors are added here.
     *
     * @param registry The {@link InterceptorRegistry} for registering interceptors.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * Adds resource handlers for serving static resources. No resource handlers are added here.
     *
     * @param registry The {@link ResourceHandlerRegistry} for registering resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * Adds view controllers for mapping view names directly to URLs. No view controllers are added here.
     *
     * @param registry The {@link ViewControllerRegistry} for registering view controllers.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    /**
     * Configures view resolvers for rendering views. No view resolvers are configured here.
     *
     * @param registry The {@link ViewResolverRegistry} for configuring view resolvers.
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        WebMvcConfigurer.super.configureViewResolvers(registry);
    }

    /**
     * Adds custom argument resolvers for handler methods. No argument resolvers are added here.
     *
     * @param resolvers The list of {@link HandlerMethodArgumentResolver} instances.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    /**
     * Adds custom return value handlers for handler methods. No return value handlers are added here.
     *
     * @param handlers The list of {@link HandlerMethodReturnValueHandler} instances.
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        WebMvcConfigurer.super.addReturnValueHandlers(handlers);
    }

    /**
     * Configures HTTP message converters. No converters are configured here.
     *
     * @param converters The list of {@link HttpMessageConverter} instances.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    /**
     * Extends the default HTTP message converters. No additional customization is applied here.
     *
     * @param converters The list of {@link HttpMessageConverter} instances.
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.extendMessageConverters(converters);
    }

    /**
     * Configures handler exception resolvers. No additional customization is applied here.
     *
     * @param resolvers The list of {@link HandlerExceptionResolver} instances.
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        WebMvcConfigurer.super.configureHandlerExceptionResolvers(resolvers);
    }

    /**
     * Extends the default handler exception resolvers. No additional customization is applied here.
     *
     * @param resolvers The list of {@link HandlerExceptionResolver} instances.
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        WebMvcConfigurer.super.extendHandlerExceptionResolvers(resolvers);
    }

    /**
     * Provides a custom validator for the application. No custom validator is provided here.
     *
     * @return A {@link Validator} instance.
     */
    @Override
    public Validator getValidator() {
        return WebMvcConfigurer.super.getValidator();
    }

    /**
     * Provides a custom message codes resolver for validation messages. No resolver is provided here.
     *
     * @return A {@link MessageCodesResolver} instance.
     */
    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return WebMvcConfigurer.super.getMessageCodesResolver();
    }
}
