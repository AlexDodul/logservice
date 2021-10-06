package org.bitbucket.logservice.repositories;


import java.util.Optional;
import org.bitbucket.logservice.entity.ChannelEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ChannelRepo extends ElasticsearchRepository<ChannelEntity, String> {

  Optional<ChannelEntity> findByUserId(String userId);

  Optional<ChannelEntity> findByChannelId(String channelId); // findByChannelIdContains - переделать в лист и использовать этот метод
}
