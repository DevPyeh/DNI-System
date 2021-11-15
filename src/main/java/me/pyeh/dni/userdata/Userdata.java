package me.pyeh.dni.userdata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Userdata {

    private UUID uuid;
    private String name;

    private String name_surname;
    private String date_of_birth;
    private String nationality;
    private String gender;
    private boolean check;

    private List<String> notes;

    public Userdata(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.name_surname = "none";
        this.date_of_birth = "none";
        this.nationality = "none";
        this.gender = "Sin Especificar";

        this.check = false;
        this.notes = new ArrayList<>();
    }
}
