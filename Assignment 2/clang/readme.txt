OS: Windows 10 running Windows Subsystem for Linux Ubuntu 17.10
GCC Version: 7.2.0 (Ubuntu 7.2.0-8ubuntu3.2)

flags: -fno-stack-protector -z execstack

disabling ASLR (run as root): echo 0 > /proc/sys/kernel/randomize_va_space