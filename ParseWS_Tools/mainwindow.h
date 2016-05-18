#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void updateText(const QString &);

private:
    Ui::MainWindow *ui;
    QString millsToDate(long mills);
};

#endif // MAINWINDOW_H
