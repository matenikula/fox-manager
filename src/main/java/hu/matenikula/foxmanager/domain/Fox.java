package hu.matenikula.foxmanager.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FOX")
@Getter
@Setter
@NoArgsConstructor
public class Fox {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foxSeqGen")
    @SequenceGenerator(name = "foxSeqGen", sequenceName = "FOX_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SPECIES")
    private String species;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false, length = 10)
    private Gender gender;

    @Column(name = "IMAGE", length = 1000)
    private String image;
}