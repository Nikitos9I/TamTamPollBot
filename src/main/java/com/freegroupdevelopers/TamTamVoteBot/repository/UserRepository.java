package com.freegroupdevelopers.TamTamVoteBot.repository;

import com.freegroupdevelopers.TamTamVoteBot.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findUserModelByIdAndUserName(long userId, String userName);

}
