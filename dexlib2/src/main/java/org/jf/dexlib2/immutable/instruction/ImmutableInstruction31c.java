/*
 * Copyright 2012, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib2.immutable.instruction;

import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.util.Preconditions;

import javax.annotation.Nonnull;

public class ImmutableInstruction31c extends ImmutableInstruction implements Instruction31c {
    public static final Format FORMAT = Format.Format31c;

    public final int registerA;
    @Nonnull public final String reference;

    public ImmutableInstruction31c(@Nonnull Opcode opcode,
                                   int registerA,
                                   @Nonnull String reference) {
        super(opcode);
        Preconditions.checkFormat(opcode, FORMAT);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.reference = Preconditions.checkReference(reference, opcode.referenceType);
    }

    public static ImmutableInstruction31c of(Instruction31c instruction) {
        if (instruction instanceof ImmutableInstruction31c) {
            return (ImmutableInstruction31c)instruction;
        }
        return new ImmutableInstruction31c(
                instruction.getOpcode(),
                instruction.getRegisterA(),
                instruction.getReference());
    }

    @Override public int getRegisterA() { return registerA; }
    @Nonnull @Override public String getReference() { return reference; }

    @Override public Format getFormat() { return FORMAT; }
}