Set oShell = CreateObject ("Wscript.Shell")
Dim strArgs
strArgs = "cmd /c tfs_launcher.bat"
oShell.Run strArgs, 0, false