package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdatingUserPasswordRes {

    private List<UserPasswordRes> userList;

    public UpdatingUserPasswordRes() {
        this.userList = new ArrayList<>();
    }
}
