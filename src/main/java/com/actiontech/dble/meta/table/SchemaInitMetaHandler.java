/*
 * Copyright (C) 2016-2020 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.actiontech.dble.meta.table;

import com.actiontech.dble.config.model.SchemaConfig;
import com.actiontech.dble.meta.ViewMeta;
import com.actiontech.dble.meta.protocol.StructureMeta;

import java.util.Set;

/**
 * Created by szf on 2019/4/4.
 * Handler for init meta of single schema
 */
public class SchemaInitMetaHandler extends AbstractSchemaMetaHandler {
    private String schema;
    private ServerMetaHandler serverMetaHandler;

    SchemaInitMetaHandler(ServerMetaHandler serverMetaHandler, SchemaConfig schemaConfig, Set<String> selfNode) {
        super(serverMetaHandler.getTmManager(), schemaConfig, selfNode, true);
        this.serverMetaHandler = serverMetaHandler;
        this.schema = schemaConfig.getName();
    }

    public void execute() {
        this.serverMetaHandler.getTmManager().createDatabase(schema);
        super.execute();
    }

    @Override
    void handleSingleMetaData(StructureMeta.TableMeta tableMeta) {
        if (tableMeta != null) {
            getTmManager().addTable(schema, tableMeta);
        }
    }

    @Override
    void handleViewMeta(ViewMeta viewMeta) {
        if (viewMeta != null) {
            getTmManager().addView(schema, viewMeta);
        }
    }

    @Override
    void handleMultiMetaData(Set<StructureMeta.TableMeta> tableMetas) {
        for (StructureMeta.TableMeta tableMeta : tableMetas) {
            if (tableMeta != null) {
                handleSingleMetaData(tableMeta);
                break;
            }
        }
    }

    @Override
    void schemaMetaFinish() {
        serverMetaHandler.countDown();
    }

}
