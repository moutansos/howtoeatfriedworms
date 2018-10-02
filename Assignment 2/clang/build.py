from subprocess import call

call(['clang', 'StackOverrun.c', '-o', 'StackOverrun', '-fno-stack-protector', '-z', 'execstack'])