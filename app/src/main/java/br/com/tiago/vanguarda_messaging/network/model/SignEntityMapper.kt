package br.com.tiago.vanguarda_messaging.network.model

import br.com.tiago.vanguarda_messaging.data.model.Sign
import br.com.tiago.vanguarda_messaging.domain.util.EntityMapper

class SignEntityMapper: EntityMapper<SignNetworkEntity, Sign> {
    override fun mapFromEntity(entity: SignNetworkEntity): Sign {
        return Sign(
            letter = entity.letter ?: "",
            url = entity.url ?: ""
        )
    }
}