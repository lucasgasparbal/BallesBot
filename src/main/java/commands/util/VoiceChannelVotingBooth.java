package commands.util;

public class VoiceChannelVotingBooth {
    private int currentVotes = 0;
    private long monitoredVoiceChannelId;
    private int neededVotes = 0;

    public VoiceChannelVotingBooth(){
        monitoredVoiceChannelId = 0;
    }
    public void addVote(){
        currentVotes++;
    }

    public int getCurrentVotes() {
        return currentVotes;
    }

    public int getNeededVotes() {
        return neededVotes;
    }

    public void updateVotesIfNeeded(long voiceChannelId, double voiceChannelMemberCount){

        if(monitoredVoiceChannelId != voiceChannelId){
            currentVotes = 0;
            monitoredVoiceChannelId = voiceChannelId;
        }

        neededVotes = (int) Math.ceil(voiceChannelMemberCount/2);


    }

    public void resetBooth(){
        currentVotes = 0;
    }

    public  boolean votingCompleted(){
        if(neededVotes == 0){
            return false;
        }

        return (currentVotes >= neededVotes);
    }
}
