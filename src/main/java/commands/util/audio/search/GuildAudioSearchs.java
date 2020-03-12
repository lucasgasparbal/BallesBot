package commands.util.audio.search;

import Exceptions.audio.search.NoTrackOnIndexException;
import Exceptions.audio.search.UserHasSearchActiveException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.util.Temporizador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildAudioSearchs {

    private Map<Long, List<AudioTrack>> searchResultsByUserId = new HashMap<>();
    private Map<Long, Temporizador> searchDestroyingTimers = new HashMap<>();

    public void addSearch(Long requesterId, List<AudioTrack> audioPlaylist) throws UserHasSearchActiveException {
        if(searchResultsByUserId.containsKey(requesterId)){
            throw new UserHasSearchActiveException();
        }
        searchResultsByUserId.put(requesterId,audioPlaylist);

        int searchResultsLifetime = 120;

        Temporizador destroyer = new Temporizador(searchResultsLifetime, ()-> destroySearch(requesterId));
        destroyer.empezar();
        searchDestroyingTimers.put(requesterId,destroyer);
    }

    public void destroySearch(Long requesterId){

        searchDestroyingTimers.remove(requesterId).parar();
        searchResultsByUserId.remove(requesterId);

    }

    public AudioTrack getSearchResultForUser(Long userId, int searchIndex) throws NoTrackOnIndexException {
        if(!searchResultsByUserId.containsKey(userId)){
            return null;
        }

        searchDestroyingTimers.remove(userId).parar();
        AudioTrack desiredTrack = searchResultsByUserId.remove(userId).get(searchIndex);
        if(desiredTrack == null){
            throw new NoTrackOnIndexException();
        }

        return  desiredTrack;
    }

    public boolean containsSearchByMember(Long memberId) {
        return(searchResultsByUserId.containsKey(memberId));
    }
}
