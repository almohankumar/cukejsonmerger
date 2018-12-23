package com.almohankumar.cukejsonmerger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@JsonPropertyOrder({"line","elements","name","description","id","keyword","uri"})
public class Report {

    private String id;

    private String description;

    private String keyword;

    private String name;

    private Integer line;

    private URI uri;

    private List<Elements> elements;

    @Data
    @JsonPropertyOrder({"before","line","name","description","id","after","type","keyword","steps","tags"})
    public static class Elements{

        private List<Tags> tags;
        private List<After> after;
        private String id;
        private String description;
        private List<Before> before;
        private String keyword;
        private String name;
        private Integer line;
        private List<Steps> steps;
        private String type;
    }

    @Data
    public static class Tags{

        private Integer line;
        private String name;

    }

    @Data
    public static class Steps{

        private Result result;
        private Integer line;
        private String name;
        private Match match;
        private String keyword;

    }

    @Data
    public static class Match{

        private String location;
    }

    @Data
    public static class Result{

        private long duration;
        private String status;
    }

    @Data
    public static class After{

        private Result result;
        private Match match;
    }

    @Data
    public static class Before{

        private Result result;
        private Match match;
    }
}

