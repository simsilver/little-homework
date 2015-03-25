namespace StringsFinder
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.matchListView = new System.Windows.Forms.ListView();
            this.matchTextBox = new System.Windows.Forms.TextBox();
            this.fileNameLabel = new System.Windows.Forms.Label();
            this.matchTextLabel = new System.Windows.Forms.Label();
            this.tipLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // matchListView
            // 
            this.matchListView.Location = new System.Drawing.Point(12, 12);
            this.matchListView.Name = "matchListView";
            this.matchListView.Size = new System.Drawing.Size(195, 238);
            this.matchListView.TabIndex = 0;
            this.matchListView.UseCompatibleStateImageBehavior = false;
            // 
            // matchTextBox
            // 
            this.matchTextBox.Location = new System.Drawing.Point(255, 44);
            this.matchTextBox.MaximumSize = new System.Drawing.Size(300, 4);
            this.matchTextBox.Name = "matchTextBox";
            this.matchTextBox.Size = new System.Drawing.Size(133, 21);
            this.matchTextBox.TabIndex = 1;
            this.matchTextBox.Text = "Model__Bone";
            this.matchTextBox.WordWrap = false;
            this.matchTextBox.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            // 
            // fileNameLabel
            // 
            this.fileNameLabel.AutoSize = true;
            this.fileNameLabel.Location = new System.Drawing.Point(213, 12);
            this.fileNameLabel.Name = "fileNameLabel";
            this.fileNameLabel.Size = new System.Drawing.Size(59, 12);
            this.fileNameLabel.TabIndex = 2;
            this.fileNameLabel.Text = "FileName:";
            // 
            // matchTextLabel
            // 
            this.matchTextLabel.AutoSize = true;
            this.matchTextLabel.Location = new System.Drawing.Point(213, 47);
            this.matchTextLabel.Name = "matchTextLabel";
            this.matchTextLabel.Size = new System.Drawing.Size(41, 12);
            this.matchTextLabel.TabIndex = 3;
            this.matchTextLabel.Text = "Match:";
            // 
            // tipLabel
            // 
            this.tipLabel.AutoSize = true;
            this.tipLabel.Location = new System.Drawing.Point(213, 82);
            this.tipLabel.Name = "tipLabel";
            this.tipLabel.Size = new System.Drawing.Size(131, 12);
            this.tipLabel.TabIndex = 4;
            this.tipLabel.Text = "Drag && Drop File Here";
            // 
            // Form1
            // 
            this.AllowDrop = true;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(433, 262);
            this.Controls.Add(this.tipLabel);
            this.Controls.Add(this.matchTextLabel);
            this.Controls.Add(this.fileNameLabel);
            this.Controls.Add(this.matchTextBox);
            this.Controls.Add(this.matchListView);
            this.Name = "Form1";
            this.Text = "Strings";
            this.DragDrop += new System.Windows.Forms.DragEventHandler(this.Form1_DragDrop);
            this.DragEnter += new System.Windows.Forms.DragEventHandler(this.Form1_DragEnter);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListView matchListView;
        private System.Windows.Forms.TextBox matchTextBox;
        private System.Windows.Forms.Label fileNameLabel;
        private System.Windows.Forms.Label matchTextLabel;
        private System.Windows.Forms.Label tipLabel;
    }
}

