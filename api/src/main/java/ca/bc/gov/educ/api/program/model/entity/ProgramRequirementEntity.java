package ca.bc.gov.educ.api.program.model.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "PROGRAM_REQUIREMENT")
@EqualsAndHashCode(callSuper=false)
public class ProgramRequirementEntity  extends BaseEntity {
   
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "PROGRAM_REQUIREMENT_ID", nullable = false)
    private UUID programRequirementID; 
	
	@Column(name = "GRADUATION_PROGRAM_CODE", nullable = false)
    private String graduationProgramCode;
	
	@OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "PROGRAM_REQUIREMENT_CODE", referencedColumnName = "PROGRAM_REQUIREMENT_CODE")
    private ProgramRequirementCodeEntity programRequirementCode;
	
	
	
	
}