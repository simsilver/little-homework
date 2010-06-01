#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DEFAULT_MAX_FILE 500
#define INDEX_LEN 4
#define NAME_LEN 8
#define APPEND_LEN 5


typedef struct file_name
{
	int index;
	char name[NAME_LEN];
	char append[APPEND_LEN];
} FileName_T;

char initals[] = "aoeiuvbpmfdtnlgkhjqxzcs";

int get_pinyin (char *buf, int count)
{
	int i = 0;
	for (i = 0; i < count; i++)
	{
		buf[i] = initals[random () % (sizeof (initals) - 1)];
	}
	return 0;
}

int generate_table (FILE * fp, int count)
{
	int i = 0;
	FileName_T *file_table = malloc (sizeof (FileName_T) * count);
	if (NULL == file_table)
	{
		return 1;
	}
	for (i = 0; i < count; i++)
	{
		memset ((char *) &file_table[i], 0, sizeof (FileName_T));
		file_table[i].index = i;
		get_pinyin ((char *) file_table[i].name,
				random () % (NAME_LEN - 1) + 1);
		sprintf ((char *) &file_table[i].append, "kok");
	}
	fwrite ((char *) file_table, sizeof (FileName_T), count, fp);
	free (file_table);
	return 0;
}

FileName_T * read_table (FILE * fp)
{
	int i = 0, count = 0;
	fseek (fp, 0, SEEK_END);
	long len = ftell (fp);
	FileName_T *file_table = malloc (len);
	count = len / sizeof (FileName_T);
	if (NULL == file_table)
	{
		return NULL;
	}
	fseek (fp, 0, SEEK_SET);
	fread (file_table, sizeof (FileName_T), count, fp);
	for (i = 0; i * sizeof (FileName_T) < len; i++)
	{
		printf ("%d - %s.%s\n", file_table[i].index, file_table[i].name,
				file_table[i].append);
	}
	return file_table;
}

int parse_args (int argc, char **argv, int *num, char **filename)
{
	switch (argc)
	{
		case 3:
			*filename = argv[2];
		case 2:
			*num = atoi (argv[1]);
		case 1:
			break;
	}
	if (NULL == *filename)
	{
		*filename = "filelist.txt";
	}
	if (0 == *num)
	{
		*num = DEFAULT_MAX_FILE;
	}
	return 0;
}


int main (int argc, char **argv)
{
	int maxfile = 0;
	char *filename = NULL;
	FILE *outfile = NULL;

	parse_args (argc, argv, &maxfile, &filename);

	outfile = fopen (filename, "w+");

	generate_table (outfile, maxfile);

	read_table (outfile);

	fclose (outfile);
	return 0;
}
