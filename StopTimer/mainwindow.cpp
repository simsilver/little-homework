#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QFile>
#include <QTimer>
#include <QDir>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    time = NULL;
    list = NULL;
    ui_timer = new QTimer();
    ui_timer->setInterval(20);
    listModel = NULL;
    setFocusPolicy(Qt::ClickFocus);
    setFocus(Qt::ActiveWindowFocusReason);
    connect(ui_timer,SIGNAL(timeout()),this,SLOT(updateLabel()));
    connect(ui->actionSave,SIGNAL(triggered(bool)),this,SLOT(saveToFile()));
}

MainWindow::~MainWindow()
{
    delete ui;
    if(time) {
        delete time;
        time = NULL;
    }
    if(ui_timer) {
        delete ui_timer;
        ui_timer = NULL;
    }
    if(list) {
        delete list;
        list = NULL;
    }
    if(listModel) {
        delete listModel;
        listModel = NULL;
    }
}

void MainWindow::keyPressEvent(QKeyEvent *event) {
    QKeyEvent::Type type = event->type();
    switch (event->key()) {
    case Qt::Key_Space:
        if(type == QKeyEvent::KeyPress) {
            on_recordButton_clicked();
        }
        break;
    case Qt::Key_Enter:
    case Qt::Key_Return:
        if(type == QKeyEvent::KeyPress) {
            on_stopButton_clicked();
        }
        break;
    default:
        QMainWindow::keyPressEvent(event);
        break;
    }
}

void MainWindow::saveToFile() {
    QFile *file = new QFile(QDir::homePath() +"/stopWatch.txt");
    file->open(QFile::Truncate|QFile::WriteOnly);
    if(list) {
        for(int i = 0; i < list->count(); i++) {
            QString s = QString::number(list->at(i), 10);
            file->write(s.toLocal8Bit());
            file->write("\n");
        }
    }
    file->close();
}

QString MainWindow::formatMills(long mills) {
    long sec = mills / 1000;
    long min = sec / 60;
    long hour = min / 60;
    QString millString =
            QString::number(hour % 24, 10)
            + ":"
            + QString::number(min % 60, 10)
            + ":"
            + QString::number(sec % 60, 10)
            + ":"
            + QString::number(mills % 1000, 10);
    return millString;
}

void MainWindow::updateLabel() {
    long mills = 0;
    if(tmpTime >= 0) {
        mills = tmpTime;
        tmpTime = -1;
    } else if(time != NULL) {
        mills = time->elapsed();
    }
    ui->nowTimeLabel->setText(formatMills(mills));
}

void MainWindow::updateListView() {
    if(listModel == NULL) {
        listModel = new QStandardItemModel();
    }
    ui->resultListView->setModel(listModel);
    if(list == NULL) {
        listModel->clear();
    }
}

void MainWindow::on_recordButton_clicked()
{
    startAndRecord();
    updateLabel();
    updateListView();
}

void MainWindow::on_stopButton_clicked()
{
    startAndRecord();
    resetTime();
    updateLabel();
    updateListView();
}

void MainWindow::resetTime() {
    if(time != NULL) {
        delete time;
        time = NULL;
        ui_timer->stop();
    }
    if(list != NULL) {
        if(list->last() != -1) {
            list->append(-1);
        }
    }
}

void MainWindow::startAndRecord() {
    tmpTime = -1;
    if(time == NULL) {
        time = new QTime();
        time->start();
        ui_timer->start();
        if(list == NULL) {
            list = new QList<long>();
        }
        if(listModel != NULL) {
            listModel->clear();
        }
    } else {
        tmpTime = time->elapsed();
        list->append(tmpTime);
        if(listModel == NULL) {
            listModel = new QStandardItemModel();
        }
        QStandardItem *item = new QStandardItem(formatMills(tmpTime));
        listModel->appendRow(item);
    }
}
