#!/usr/bin/env bash
# Actualiza el parámetro de cache-busting (&cache=<hash>) de cada diagrama
# .puml embebido en README.md vía el proxy de PlantUML, para forzar un
# render nuevo cuando el .puml cambia (el proxy cachea el SVG por URL).
set -euo pipefail

repo_root="$(git rev-parse --show-toplevel)"
readme="$repo_root/README.md"

[ -f "$readme" ] || exit 0

changed=0

mapfile -t rel_paths < <(grep -oE 'raw\.githubusercontent\.com/[^/]+/[^/]+/[^/]+/[^&)]+\.puml' "$readme" \
    | sed -E 's#^raw\.githubusercontent\.com/[^/]+/[^/]+/[^/]+/##' \
    | sort -u)

for rel_path in "${rel_paths[@]}"; do
    local_file="$repo_root/$rel_path"
    [ -f "$local_file" ] || continue

    hash="$(git hash-object "$local_file" | cut -c1-8)"
    escaped="$(printf '%s' "$rel_path" | sed -E 's/[.]/\\./g')"

    if grep -qE "${escaped}&fmt=svg&cache=[0-9a-f]+" "$readme"; then
        sed -i -E "s#(${escaped}&fmt=svg)&cache=[0-9a-f]+#\1\&cache=${hash}#" "$readme"
    else
        sed -i -E "s#(${escaped}&fmt=svg)#\1\&cache=${hash}#" "$readme"
    fi
    changed=1
done

if [ "$changed" -eq 1 ]; then
    git add "$readme"
fi
