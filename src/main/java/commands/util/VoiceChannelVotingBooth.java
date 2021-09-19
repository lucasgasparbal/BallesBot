package commands.util;

import java.util.HashSet;
import java.util.Set;

public class VoiceChannelVotingBooth {
    private int currentVotes = 0;
    private Set<Long> usersThatVoted = new HashSet<>();
    private int neededVotes = 0;
    public void addVote(long userId, int voteTreshhold){
        if (userHasAlreadyVoted(userId)){
            return;
        }
        neededVotes = voteTreshhold;
        currentVotes++;
        usersThatVoted.add(userId);
    }

    public int getCurrentVotes() {
        return currentVotes;
    }
    public int getNeededVotes() {
        return neededVotes;
    }


    public void resetBooth(){
        currentVotes = 0;
        usersThatVoted.clear();
    }

    public  boolean votingComplete(){
        if(neededVotes == 0){
            return false;
        }

        return (currentVotes >= neededVotes);
    }

    public boolean userHasAlreadyVoted(long userId){
        return usersThatVoted.contains(userId);
    }
}
