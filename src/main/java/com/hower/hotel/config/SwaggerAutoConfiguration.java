/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hower.hotel.config;

import java.util.Collections;
import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * RESTful ?????? API ???????????? Swagger ???????????????
 * </p>
 *
 * 
 */
@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(getParameters())
                .apiInfo(apiInfo())
                .select()
                //?????????@Api???
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // ?????????@ApiOperation???
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(true);
    }

    /**
     * ??????swagger ApiInfo
     *
     * @return
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Crown API")
                .description("Crown Swagger API ??????")
                .termsOfServiceUrl("https://github.com/Caratacus/Crown")
                .version("1.0")
                .contact(new Contact("Crown", "https://github.com/Caratacus", "caratacus@qq.cn"))
                .build();
    }

    /**
     * ??????Swagger??????
     *
     * @return
     */
    List<Parameter> getParameters() {
        return Collections.singletonList(new ParameterBuilder()
                .name("Authorization")
//                .defaultValue("Bearer " + JWTUtils.generate(1))
                .description("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
    }
    /**
     * ??????@EnableMvc???????????????????????????????????????????????????????????????
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ??????????????????????????????
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // ??????swagger????????????
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // ??????swagger???js??????????????????
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
