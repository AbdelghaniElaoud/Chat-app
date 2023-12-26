package dto;

import entity.Cours;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursDto {
    private String nom;
    private int coefficient;
    private String sigle;

    public static CoursDto fromCours(Cours cours) {
        CoursDto coursDto = new CoursDto();
        coursDto.setNom(cours.getNom());
        coursDto.setCoefficient(cours.getCoefficient());
        coursDto.setSigle(cours.getSigle());
        return coursDto;
    }
}
