#!/usr/bin/env bash

# El shebang sirve para decirle al sistema operativo qué programa debe usar
# para ejecutar el código a continuación.

# El siguiente script, escrito en el lenguaje de scripting de shell de Bash,
# actualiza el parámetro de cache-busting (&cache=<hash>)
# de cada diagrama .puml embebido en README.md vía el proxy de PlantUML, para
# forzar un render nuevo cuando el .puml cambia (el proxy cachea el SVG por URL).

set -e -u -o pipefail

# `set -e -u -o pipefail` configura flags para modificar el comportamiento del script:
#       - El script termina cuando un comando falla (con ciertas excepciones de Bash).
#       - El script termina al referenciar una variable no definida.
#       - Las fallas dentro de pipelines son detectables: si cualquiera de los
#           comandos falla, el pipeline devuelve un código de salida distinto de cero.
#           Combinado con `-e`, esto generalmente provoca la terminación del script.

repo_root="$(git rev-parse --show-toplevel)"
readme="$repo_root/README.md"

# `rev-parse` muestra información importante sobre el repositorio. `--show-toplevel` hace
# referencia al path absoluto del directorio raíz del repositorio.

[ -f "$readme" ] || exit 0 

# `-f` es útil para chequear que un path en particular existe y es un archivo regular
# (no un directorio).

changed=0

# El siguiente código encadena varios comandos usando `|`.

mapfile -t rel_paths < <(grep -oE 'raw\.githubusercontent\.com/[^/]+/[^/]+/[^/]+/[^&)]+\.puml' "$readme" \
    | sed -E 's#^raw\.githubusercontent\.com/[^/]+/[^/]+/[^/]+/##' \
    | sort -u)

# El output del pipeline se convierte en el input de `mapfile`.
# `-t` elimina el newline final de cada línea.
# `mapfile` es un comando built-in de Bash que lee líneas y las guarda en un array.
# `rel_paths` es el nombre del array.
# El propósito del código previo es, en resumidas cuentas, construir un array
# de paths relativos de los archivos .puml referenciados en el README.md
# mediante URLs de raw.githubusercontent.com que coincidan con el patrón definido.

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
