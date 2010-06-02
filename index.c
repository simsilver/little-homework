#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <time.h>

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

typedef struct index_tree
{
    int index;
    char *key;
    struct index_tree *left;
    struct index_tree *right;
} IndexTree_T;

typedef int (*CompareFunc) (void *, void *);
char initals[] = "aoeiuvbpmfdtnlgkhjqxzcs";
char *default_file = "filelist.txt";
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
        return 1;
    for (i = 0; i < count; i++)
    {
        memset ((char *) &file_table[i], 0, sizeof (FileName_T));
        file_table[i].index = i;
        get_pinyin ((char *) file_table[i].name,
                    random () % (NAME_LEN - 1) + 1);
        sprintf ((char *) &file_table[i].append, "kok");
    }
    fseek (fp, 0, SEEK_SET);
    fwrite ((char *) file_table, sizeof (FileName_T), count, fp);
    free (file_table);
    return 0;
}

int print_table (FILE * fp)
{
    int i = 0, count = 0;
    long len = 0;
    FileName_T *file_table = NULL;
    fseek (fp, 0, SEEK_END);
    len = ftell (fp);
    file_table = malloc (len);
    count = len / sizeof (FileName_T);
    if (NULL == file_table)
        return 1;
    fseek (fp, 0, SEEK_SET);
    fread (file_table, sizeof (FileName_T), count, fp);
    for (i = 0; i * sizeof (FileName_T) < len; i++)
    {
        printf ("%d - %s.%s\n", file_table[i].index, file_table[i].name, file_table[i].append);
    }
    free (file_table);
    return 0;
}

int compare_name (IndexTree_T * first, IndexTree_T * second)
{
    int i = 0;
    assert (NULL != first);
    assert (NULL != second);
    do
    {
        if (first->key[i] > second->key[i])
            return 1;
        else if (first->key[i] < second->key[i])
            return -1;
        else if ((first->key[i] == 0) && (first->key[i] == second->key[i]))
            return 0;
    }
    while (i++);

    return 0;
}

int travel_tree (IndexTree_T * node)
{
    if (NULL != node)
    {
        if (NULL != node->left)
        {
            travel_tree (node->left);
        }
        printf ("%5d\t", node->index);
        if (NULL != node->right)
        {
            travel_tree (node->right);
        }
        printf ("\n");
        return 0;
    }
    return 1;
}

int simple_insert (IndexTree_T * root, IndexTree_T * cur, CompareFunc func)
{
    IndexTree_T *ptr = root;
    do
    {
        if (NULL == ptr || NULL == cur)
        {
            return 1;
        }
        if (0 <= func (ptr, cur))
        {
            if (NULL == ptr->left)
            {
                ptr->left = cur;
                break;
            }
            else
            {
                ptr = ptr->left;
            }
        }
        else
        {
            if (NULL == ptr->right)
            {
                ptr->right = cur;
                break;
            }
            else
            {
                ptr = ptr->right;
            }
        }
    }
    while (1);
    return 0;
}

IndexTree_T *simple_GetTree (FILE * fp, CompareFunc func)
{
    IndexTree_T *root = NULL;
    FileName_T filename = { 0 };
    fseek (fp, 0, SEEK_SET);
    fread (&filename, sizeof (FileName_T), 1, fp);
    root = malloc (sizeof (IndexTree_T));
    if (NULL == root)
    {
        return NULL;
    }
    memset (root, 0, sizeof (IndexTree_T));
    root->index = filename.index;
    root->key = malloc (strlen (filename.name) + 1);
    if (NULL == root->key)
    {
        free (root);
        return NULL;
    }
    strcpy (root->key, filename.name);
    while (fread (&filename, sizeof (FileName_T), 1, fp))
    {
        IndexTree_T *cur = malloc (sizeof (IndexTree_T));
        if (NULL == cur)
        {
            break;
        }
        memset (cur, 0, sizeof (IndexTree_T));
        cur->index = filename.index;
        cur->key = malloc (strlen (filename.name) + 1);
        if (NULL == cur->key)
        {
            free (cur);
            break;
        }
        strcpy (cur->key, filename.name);

        simple_insert (root, cur, func);
    }

    return root;
}

int main (int argc, char **argv)
{
    int maxfile = DEFAULT_MAX_FILE;
    char *filename = default_file;
    FILE *outfile = NULL;
    IndexTree_T *root = NULL;
    if (argc >= 2)
    {
        if (!(maxfile = atoi (argv[1])))
        {
            maxfile = DEFAULT_MAX_FILE;
        }
        if ((argc >= 3)&&!(filename = argv[2]))
        {
            filename = default_file;
        }
    }
	srandom(time(NULL));
    outfile = fopen (filename, "w+");
    generate_table (outfile, maxfile);
    print_table (outfile);
    root = simple_GetTree (outfile, (CompareFunc) compare_name);
    travel_tree (root);
    fclose (outfile);
    return 0;
}
