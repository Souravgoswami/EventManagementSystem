#!/usr/bin/ruby -w
# Frozen_String_Literal: true
%w(sqlite3 io/console timeout).each(&method(:require))

DBFile = Dir.pwd + '/info.db'

trap('SIGINT') { abort("\nExiting...") }

print <<~EOF
	\e[1;34mAre you sure about recreating the database?\e[0m
	\e[1;36mThis will delete all the entries.
	\e[1;33mPress \e[1;32mY\e[1;33m to confirm:\e[0m
EOF

fifo = false

begin
	Timeout.timeout(0.000_000_001) { fifo = STDIN.getc }
rescue Timeout::Error
end

unless fifo
	exit 0 if STDIN.getch.upcase != ?Y 
else
	exit 0 unless p fifo.upcase.eql?(?Y)
end

Object.prepend(SQLite3)

if File.exists?(DBFile)
	puts "\e[1;31mDeleting #{DBFile}\e[0m"
	File.delete(DBFile) rescue puts("\e[32mCannot delete #{DBFile}\e[0m")
end

puts "\e[1;33mCreating database #{DBFile}\e[0m"
database = Database.new(DBFile)

database.execute <<~'EOF'
	CREATE TABLE IF NOT EXISTS Participants(
		id INTEGER PRIMARY KEY,
		name TEXT NOT NULL,
		password TEXT NOT NULL,
		class TEXT NOT NULL,
		section TEXT NOT NULL,
		roll TEXT NOT NULL,
		gender INTEGER NOT NULL,
		event TEXT NOT NULL,
		phone TEXT NOT NULL,
		email TEXT NOT NULL
	) ;
EOF

puts "\e[1;32mSuccessfully created #{DBFile}\e[0m"
