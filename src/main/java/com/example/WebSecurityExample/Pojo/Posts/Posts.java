package com.example.WebSecurityExample.Pojo.Posts;

import com.example.WebSecurityExample.Pojo.Contest;
import com.example.WebSecurityExample.Pojo.UserContestDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.WebSecurityExample.Pojo.Course.Course;

import java.util.*;

@Data
@Document(collection = "Posts")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Posts {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @EqualsAndHashCode.Include
    @NonNull
    private String title;
    @NonNull
    private String description;
    private String userName;
    private String Example;
    private String difficulty;
    private Map<String, SolutionCode> solution=new HashMap<>();;
    private List<String> answer = new ArrayList<>();
    private List<String> companies = new ArrayList<>();
    private String accuracy ;
    private String constrain;
    private String timecomplixity;
    private String avgtime;
    private Date lastModified;
    private String type;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String videoUrl;
    private String sequence;
    private String input;

    @JsonIgnore
    @DBRef
    private Course course;

    @JsonIgnore
    @DBRef
    private Contest contest;

    @JsonIgnore
    @DBRef
    private UserContestDetails userContestDetails;

    private List<String> tags=new ArrayList<>();

    private Map<String, String> testcase = new HashMap<>();

    private Map<String, HelperCode> codeTemplates = new HashMap<>();
}
