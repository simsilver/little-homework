using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Collections;

namespace StringsFinder
{
    public partial class Form1 : Form
    {
        private String mFileName = "";
        private ArrayList mAllString = new ArrayList();
        private int mMinLength = 4;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_DragEnter(object sender, System.Windows.Forms.DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
                e.Effect = DragDropEffects.Link;
            else e.Effect = DragDropEffects.None;

            mFileName = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
            int pos = mFileName.LastIndexOf('\\');
            if (pos < 0)
            {
                pos = 0;
            }
            fileNameLabel.Text = "FileName: " + mFileName.Substring(pos + 1);
        }

        private void Form1_DragDrop(object sender, System.Windows.Forms.DragEventArgs e)
        {
            mFileName = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
            ProceeFile(mFileName);
            ProcessStrings();
        }

        private void ProcessStrings()
        {
            String matchText = matchTextBox.Text;
            if (matchText.Length < mMinLength)
            {
                return;
            }
            String nowText;
            int size = mAllString.Count;
            int idx = 0;
            matchListView.Items.Clear();
            matchListView.View = View.List;
            for (int i = 0; i < size; i++)
            {
                nowText = (String)mAllString[i];
                idx = nowText.IndexOf(matchText);
                if (idx >= 0)
                {
                    ListViewItem item = new ListViewItem();
                    item.Text = nowText;
                    matchListView.Items.Add(item);
                }
            }
        }

        private void ProceeFile(String fileName)
        {
            mAllString.Clear();
            FileStream aFile = new FileStream(fileName, FileMode.Open);
            int c = 0;
            StringBuilder sb = new StringBuilder();
            try
            {
                while ((c = aFile.ReadByte()) >= 0)
                {
                    if (c > 31 && c < 127)
                    {
                        sb.Append((char)c);
                    }
                    else if (sb.Length > 3)
                    {
                        mAllString.Add(sb.ToString());
                        sb = new StringBuilder();
                    }
                }
                if (sb.Length > 0)
                {
                    mAllString.Add(sb.ToString());
                    sb = new StringBuilder();
                }
            }
            catch (Exception)
            {

            }
            aFile.Close();
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            if (matchTextBox.Text.Length < mMinLength)
            {
                tipLabel.Text = "Match Length shoould > " + mMinLength;
            }
            else
            {
                tipLabel.Text = "Drag && Drop File Here.";
                ProcessStrings();
            }
        }

    }
}
