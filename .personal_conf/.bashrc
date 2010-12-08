# .bashrc

export PATH=~/.local/bin:~/.cabal/bin:$PATH
# User specific aliases and functions

alias rm="rm -i"
alias cp="cp -i"
alias mv="mv -i"

alias ls="ls --color=auto"
alias ll="ls -l"

alias grep="grep --color=auto"

function ..(){
	 if [ $# -eq 0 ];
	 then 
		 cd ..; 
	 else
		  i=$1;
		  tmppath="";
		  while [ $i -gt 0 ];
		  do 
			  tmppath=../$tmppath; 
			  i=$(($i-1));
		  done; 
		  cd $tmppath;
	 fi
}

function getMakefile(){
    cp ~/Templates/Makefile.example ./Makefile
    echo Generic Makefile copied here. \(`pwd`\)
}

