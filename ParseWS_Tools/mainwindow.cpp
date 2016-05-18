#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <qdatetime.h>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    connect(ui->lineEdit,SIGNAL(textChanged(const QString &)),this,SLOT(updateText(const QString &)));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::updateText(const QString & text)
{
    long mills = text.toLong();
    ui->textBrowser->setText(millsToDate(mills));
    ui->lineEdit->selectAll();
}

QString MainWindow::millsToDate(long mills)
{
    QDateTime *time = new QDateTime();
    time->setTime_t(mills);
    QString textDate = time->toString(Qt::SystemLocaleLongDate);
    delete time;
    return textDate;
}
