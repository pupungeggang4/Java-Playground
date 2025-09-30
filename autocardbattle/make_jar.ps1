param (
    [string]$path
)
jar -cvmf manifest-win64.txt release/main.jar *.class; Copy-Item -path $path -destination release -recurse -force