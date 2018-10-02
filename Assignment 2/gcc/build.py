from subprocess import call

call(['gcc', 'StackOverrun.c', '-o', 'StackOverrun', '-fno-stack-protector', '-z', 'execstack'])