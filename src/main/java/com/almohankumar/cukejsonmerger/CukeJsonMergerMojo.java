package com.almohankumar.cukejsonmerger;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Mojo(name = "mergejson",defaultPhase = LifecyclePhase.TEST)
public class CukeJsonMergerMojo extends AbstractMojo
{


    public void execute() throws MojoExecutionException

    {
        String cwd = System.getProperty("user.dir");
        String jsonDir = "/target/cucumber-parallel";
        String dirPath = cwd+jsonDir;
        List<String> files = new ArrayList<>();

        files = getFileNames(dirPath);
        mergeAndWrite(files,dirPath);

    }




    private List<String> getFileNames(String path) throws MojoExecutionException {

        List<String> files = new ArrayList<>();

        try {
            files =  Files.list(Paths.get(path))
                    .map(Path::toFile)
                    .map(File::getName)
                    .filter(f->f.endsWith(".json"))
                    .collect(Collectors.toList());
        } catch (IOException e) {

            throw new MojoExecutionException( "Error reading .json files from 'target/cucumber-parallel'",e);

        }

        getLog().info(files.size()+" files are ready to be processed!");

        return files;

    }

    private void mergeAndWrite(List<String> files, String path) throws MojoExecutionException {

        getLog().info("**************************************************************************");
        getLog().info("*                   Cuke Json Merger                                     *");
        getLog().info("**************************************************************************");

       List<Report> reports = new ArrayList<>();

        if(!files.isEmpty()){

            files.forEach(file->{
                try {

                    URL url = Paths.get(path, file).toUri().toURL();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Report> report = Arrays.asList(objectMapper.readValue(url, Report[].class));
                    reports.addAll(report);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }

        ObjectMapper objectMapper = new ObjectMapper();


        try {

            objectMapper.writeValue(new File("target/cucumber.json"),reports);

            getLog().info("Success!, Merged "+files.size() +" files into 'target/cucumber.json'");

        } catch (IOException e) {
            throw new MojoExecutionException( "Error creating file cucumber.json",e);
        }


    }



}
