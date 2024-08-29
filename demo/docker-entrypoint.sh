#!/bin/bash
set -e

# 初期化SQLスクリプトの実行
if [ -f "./init.sql" ]; then
    psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -a -f "./init.sql"
fi

# DockerfileのCMDを実行
exec "$@"