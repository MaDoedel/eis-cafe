package com.example.shop.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "jobRequest")
public class JobRequest {
    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Gets deleted 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;

    @Column(name = "comment", unique = true, length = 255)
    private String comment; 

    @Column(name = "jobDescription", length = 32)
    @NotBlank(message = "Invalid jobDescription")
    private String jobDescription; 

    /* 
    @Column(name = "Date Interval")
    @NotBlank(message = "Invalid Inteval")
    private String Interval; 
    */

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

     // Constructors
     public JobRequest() {
    }

    public JobRequest(User user, File file, String comment, String jobDescription) {
        this.user = user;
        this.file = file;
        this.comment = comment;
        this.jobDescription = jobDescription;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
