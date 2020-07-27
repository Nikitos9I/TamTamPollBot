package com.freegroupdevelopers.TamTamVoteBot.repository;

import com.freegroupdevelopers.TamTamVoteBot.models.PollModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

public interface PollRepository extends JpaRepository<PollModel, Long> {

    PollModel findBySession(String session);
    PollModel findByMessageId(String messageId);

}
