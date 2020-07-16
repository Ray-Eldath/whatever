SECTION header vstart=0
    program_length  dd program_end
    entry_point     dw start
                    dd section.code.start

    realloc_tbl_len dw (header_end-realloc_begin)/4

    ; realloc
    realloc_begin:
    code_segment    dd section.code.start
    data_segment    dd section.data.start
    stack_segment   dd section.stack.start

    header_end:

SECTION code align=16 vstart=0
print_rtc:
    push ax

    ; year
    mov al, 0x09
    call .print_port_data
    mov si, date_separator
    call .print_char

    ; month
    mov al, 0x08
    call .print_port_data
    mov si, date_separator
    call .print_char

    ; day
    mov al, 0x07
    call .print_port_data

    mov si, blank
    call .print_char

    ; hour
    mov al, 0x04
    call .print_port_data
    mov si, time_separator
    call .print_char

    ; minute
    mov al, 0x02
    call .print_port_data
    mov si, time_separator
    call .print_char

    ; second
    mov al, 0x00
    call .print_port_data

    ; touch register C
    mov al, 0x0c
    out 0x70, al  ; register C
    in al, 0x71

    sub di, 34

    ; send EOI
    mov al, 0x20
    out 0x20, al  ; master
    out 0xa0, al  ; slave

    pop ax
    iret

    .print_port_data:  ; input port: al
    out 0x70, al
    in al, 0x71
    call .print_bcd_byte
    ret

    .print_bcd_byte:  ; input: al
    push ax
    mov ah, al
    shr ah, 4
    and al, 0x0f
    add ah, 0x30
    add al, 0x30
    mov byte [es:di], ah
    inc di
    mov byte [es:di], 0x0b
    inc di
    mov byte [es:di], al
    inc di
    mov byte [es:di], 0x0b
    inc di
    pop ax
    ret

    .print_char:  ; input: address at si
    push si
    push cx
    cld
    mov cx, 1
    movsw
    pop cx
    pop si
    ret

start:

    mov ax, [stack_segment]
    mov ss, ax
    mov sp, stack_end

    mov ax, [data_segment]
    mov ds, ax

    mov si, prompt
    mov ax, 0xb800
    mov es, ax
    xor di, di

    .print_char:
    mov al, [si]
    inc si
    or al, al
    jz .print_end
    mov [es:di], al
    inc di
    mov byte [es:di], 0x07
    inc di
    jmp .print_char
    .print_end:

    ; install intrrupt handling code
    cli
    push es
    mov ax, 0x00
    mov es, ax
    mov word [es:0x1c0], print_rtc
    mov word [es:0x1c2], cs
    pop es

    ; enable interruption in RTC
    mov al, 0x0b  ; register B
    out 0x70, al
    mov al, 0x12
    out 0x71, al
    mov al, 0x0c  ; touch C
    out 0x70, al
    in al, 0x71

    ; enable RTC interruption in 8259
    in al, 0xa1
    and al, 0xfe
    out 0xa1, al  ; slave

    sti
    .halt:
    hlt
    not byte [es:0x01]
    jmp .halt


SECTION data align=16 vstart=0
    prompt db 'Current time: '
           db 0
    date_separator db '-', 0x0b
    blank db ' ', 0x07
    time_separator db ':', 0x0b

SECTION stack align=16 vstart=0
    resb 256
stack_end:

SECTION tail
program_end: