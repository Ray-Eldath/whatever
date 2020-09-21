//
// Created by Ray Eldath on 2020/9/13.
//

#ifndef CPP20_PIMPL_H
#define CPP20_PIMPL_H

#endif //CPP20_PIMPL_H

class Widget {
private:
    struct Impl;
    std::unique_ptr<Impl> pImpl;

public:
    Widget();

    ~Widget();

    void hello();
};