package ru.knowledgebase.modelsmodule;

import javax.persistence.*;

/**
 * Created by root on 17.08.16.
 */
@Entity
public class UserSectionRole {

    @Id
    @SequenceGenerator(name="user_section_role_id_seq",
            sequenceName="user_section_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_section_role_id_seq")
    private int id;

    @OneToOne(cascade = {CascadeType.MERGE})
    private User user;

    @Column
    private int sectionId;

    @OneToOne(cascade = {CascadeType.MERGE})
    private SectionRole sectionRole;

    public UserSectionRole(){

    }

}
