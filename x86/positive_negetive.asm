jmp start
    data dw 0x90, 0xfff0, 0xa0, 0x1235, 0xf, 0xc0, 0xc5bc, 0
    prompt db '[positive] [negetive]: '  ; 5 2
    sep db ' ', 0x9F
start:
    mov ax, 0x7c0
    mov ds, ax
    mov ax, 0xb800
    mov es, ax
    mov ax, 0
    mov di, 0

    mov cx, sep - prompt  ; loop control
    mov si, prompt
showprompt:
    mov al, [si]
    mov [es:di], al  ; mov [es:di], si
    inc di
    mov byte [es:di], 0x07
    inc di
    inc si
    loop showprompt
    mov ax, 0
    mov bx, 0

    mov cx, (prompt - data) / 2 ; word
    mov bx, data
cal:
    mov dx, [bx]
    cmp dx, 0
    jg pos
    jl neg
    jmp loop_end
pos: inc ah
     jmp loop_end
neg: inc al
loop_end:
    inc bx
    inc bx
    loop cal

showpos:
    mov dl, ah
    add dl, 0x30
    mov dh, 0x9F
    mov [es:di], dx
    add di, 2
showsep:
    mov cx, start - sep
    cld
    mov si, sep
    rep movsb
showneg:
    mov dl, al
    add dl, 0x30
    mov dh, 0x9F
    mov [es:di], dx

end:
    jmp near $
    times 510-($-$$) db 0
    db 0x55, 0xaa
