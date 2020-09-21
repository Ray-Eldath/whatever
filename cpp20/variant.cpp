//
// Created by Ray Eldath on 2020/8/12.
//

#include <variant>
#include <string>
#include <vector>
#include <iostream>
#include <functional>

using namespace std;

template<class... Ts>
struct overloaded : Ts ... {
    using Ts::operator()...;
};

template<class... Ts> overloaded(Ts...)->overloaded<Ts...>;

void t(const std::function<int(int)> &f) {
    cout << "f output" << f(1) << endl;
}

int main() {
    t([](int i) -> int { return i; });
    vector<variant<string, int, double>> vec{1, 2.5, "str"};

    for (auto &v:vec)
        visit(overloaded{
                [](int i) { cout << "int " << i << endl; },
                [](const string &str) { cout << "string " << str << endl; },
                [](double d) { cout << "double " << d << endl; }
        }, v);
}