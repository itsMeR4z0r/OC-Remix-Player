package com.r4z0r.ocremixplayer.db.boxes;

import com.r4z0r.ocremixplayer.db.models.RemixObj;
import com.r4z0r.ocremixplayer.db.models.RemixObj_;

import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

public class RemixBox extends AbstractBox<RemixObj> {
    public boolean containsId(String id) {
        try (Query<RemixObj> query = getBox.query().contains(RemixObj_.remixId, id, QueryBuilder.StringOrder.CASE_INSENSITIVE).build()) {
            return query.findUnique() != null;
        }
    }

    public RemixBox() {
        super();
    }

    public RemixObj get(String id) {
        try (Query<RemixObj> query = getBox.query().contains(RemixObj_.remixId, id, QueryBuilder.StringOrder.CASE_INSENSITIVE).build()) {
            return query.findUnique();
        }
    }

}
