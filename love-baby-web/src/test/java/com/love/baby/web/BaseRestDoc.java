package com.love.baby.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;


@Component
public class BaseRestDoc {

    protected MockMvc mockMvc;

    protected String token = "";

    @Autowired
    protected WebApplicationContext context;

    //项目地址
    private final String appDir = System.getProperty("user.dir");
    //测试文件输出地址
    private final String adocPath = appDir + File.separator + "src" + File.separator + "main" + File.separator + "asciidoc" + File.separator + "interface.adoc";
    //
    private final String docExportDir = appDir + File.separator + "target" + File.separator + "generated-snippets";

    @Rule

    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(docExportDir);

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                        .snippets().withTemplateFormat(TemplateFormats.asciidoctor()).withEncoding("UTF-8")
                        .and().uris().withScheme("http").withHost("love-baby.vip").withPort(80)
                        .and().operationPreprocessors().withResponseDefaults(Preprocessors.prettyPrint())
                ).build();
    }


    @After
    public void adocBuild() throws IOException {
        StringBuilder content = new StringBuilder();
        includeTableOfContent(content);
        File apiDirs = new File(docExportDir);
        if (!apiDirs.exists()) {
            apiDirs.mkdir();
        }
        Integer i = 1;
        for (File apiDir : apiDirs.listFiles()) {
            String apiName = apiDir.getName();
            content.append("=== ").append(i + "、" + apiName).append(System.getProperty("line.separator"));
            File[] listFile = apiDir.listFiles();
            for (File file : listFile) {
                fileAppend(content, file.getPath(), file.getName());
            }
            i++;
        }
        File file = new File(adocPath);
        writeStringToFile(file, content.toString());
    }

    private void includeTableOfContent(StringBuilder content) throws FileNotFoundException {
        content.append("include::").append(appDir).append(File.separator).append("src").append(File.separator).
                append("main").append(File.separator).append("resources").append(File.separator).append("doc").
                append(File.separator).append("preview.adoc[]").
                append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
    }

    private void writeStringToFile(File file, String content) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.write(content);
        osw.flush();
    }

    private void fileAppend(StringBuilder content, String includeFileDir, String title) {
        File file = new File(includeFileDir);
        if (file.exists()) {
            content.append(title).append(System.getProperty("line.separator"));
            content.append("include::").append(includeFileDir).append("[]").append(System.getProperty("line.separator"));
        }
    }

}