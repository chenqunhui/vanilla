// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package com.vanilla.remoting.spi.codec.protobuf;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:push.Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 id = 1;</code>
     */
    long getId();

    /**
     * <code>int32 type = 2;</code>
     */
    int getType();

    /**
     * <code>int64 timestamp = 3;</code>
     */
    long getTimestamp();

    /**
     * <code>string data = 4;</code>
     */
    java.lang.String getData();
    /**
     * <code>string data = 4;</code>
     */
    com.google.protobuf.ByteString
        getDataBytes();

    /**
     * <code>bool completed = 5;</code>
     */
    boolean getCompleted();
  }
  /**
   * Protobuf type {@code push.Message}
   */
  public  static final class Message extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:push.Message)
      MessageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Message() {
      id_ = 0L;
      type_ = 0;
      timestamp_ = 0L;
      data_ = "";
      completed_ = false;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              id_ = input.readInt64();
              break;
            }
            case 16: {

              type_ = input.readInt32();
              break;
            }
            case 24: {

              timestamp_ = input.readInt64();
              break;
            }
            case 34: {
              java.lang.String s = input.readStringRequireUtf8();

              data_ = s;
              break;
            }
            case 40: {

              completed_ = input.readBool();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.vanilla.remoting.spi.codec.protobuf.MessageProto.internal_static_push_Message_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.vanilla.remoting.spi.codec.protobuf.MessageProto.internal_static_push_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.class, com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <code>int64 id = 1;</code>
     */
    public long getId() {
      return id_;
    }

    public static final int TYPE_FIELD_NUMBER = 2;
    private int type_;
    /**
     * <code>int32 type = 2;</code>
     */
    public int getType() {
      return type_;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 3;
    private long timestamp_;
    /**
     * <code>int64 timestamp = 3;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }

    public static final int DATA_FIELD_NUMBER = 4;
    private volatile java.lang.Object data_;
    /**
     * <code>string data = 4;</code>
     */
    public java.lang.String getData() {
      java.lang.Object ref = data_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        data_ = s;
        return s;
      }
    }
    /**
     * <code>string data = 4;</code>
     */
    public com.google.protobuf.ByteString
        getDataBytes() {
      java.lang.Object ref = data_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        data_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int COMPLETED_FIELD_NUMBER = 5;
    private boolean completed_;
    /**
     * <code>bool completed = 5;</code>
     */
    public boolean getCompleted() {
      return completed_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (type_ != 0) {
        output.writeInt32(2, type_);
      }
      if (timestamp_ != 0L) {
        output.writeInt64(3, timestamp_);
      }
      if (!getDataBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, data_);
      }
      if (completed_ != false) {
        output.writeBool(5, completed_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (type_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, type_);
      }
      if (timestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, timestamp_);
      }
      if (!getDataBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, data_);
      }
      if (completed_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(5, completed_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message)) {
        return super.equals(obj);
      }
      com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message other = (com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message) obj;

      boolean result = true;
      result = result && (getId()
          == other.getId());
      result = result && (getType()
          == other.getType());
      result = result && (getTimestamp()
          == other.getTimestamp());
      result = result && getData()
          .equals(other.getData());
      result = result && (getCompleted()
          == other.getCompleted());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getType();
      hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTimestamp());
      hash = (37 * hash) + DATA_FIELD_NUMBER;
      hash = (53 * hash) + getData().hashCode();
      hash = (37 * hash) + COMPLETED_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getCompleted());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code push.Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:push.Message)
        com.vanilla.remoting.spi.codec.protobuf.MessageProto.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.vanilla.remoting.spi.codec.protobuf.MessageProto.internal_static_push_Message_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.vanilla.remoting.spi.codec.protobuf.MessageProto.internal_static_push_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.class, com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.Builder.class);
      }

      // Construct using com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        id_ = 0L;

        type_ = 0;

        timestamp_ = 0L;

        data_ = "";

        completed_ = false;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.vanilla.remoting.spi.codec.protobuf.MessageProto.internal_static_push_Message_descriptor;
      }

      public com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message getDefaultInstanceForType() {
        return com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.getDefaultInstance();
      }

      public com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message build() {
        com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message buildPartial() {
        com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message result = new com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message(this);
        result.id_ = id_;
        result.type_ = type_;
        result.timestamp_ = timestamp_;
        result.data_ = data_;
        result.completed_ = completed_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message) {
          return mergeFrom((com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message other) {
        if (other == com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message.getDefaultInstance()) return this;
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        if (other.getType() != 0) {
          setType(other.getType());
        }
        if (other.getTimestamp() != 0L) {
          setTimestamp(other.getTimestamp());
        }
        if (!other.getData().isEmpty()) {
          data_ = other.data_;
          onChanged();
        }
        if (other.getCompleted() != false) {
          setCompleted(other.getCompleted());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long id_ ;
      /**
       * <code>int64 id = 1;</code>
       */
      public long getId() {
        return id_;
      }
      /**
       * <code>int64 id = 1;</code>
       */
      public Builder setId(long value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 id = 1;</code>
       */
      public Builder clearId() {
        
        id_ = 0L;
        onChanged();
        return this;
      }

      private int type_ ;
      /**
       * <code>int32 type = 2;</code>
       */
      public int getType() {
        return type_;
      }
      /**
       * <code>int32 type = 2;</code>
       */
      public Builder setType(int value) {
        
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 type = 2;</code>
       */
      public Builder clearType() {
        
        type_ = 0;
        onChanged();
        return this;
      }

      private long timestamp_ ;
      /**
       * <code>int64 timestamp = 3;</code>
       */
      public long getTimestamp() {
        return timestamp_;
      }
      /**
       * <code>int64 timestamp = 3;</code>
       */
      public Builder setTimestamp(long value) {
        
        timestamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 timestamp = 3;</code>
       */
      public Builder clearTimestamp() {
        
        timestamp_ = 0L;
        onChanged();
        return this;
      }

      private java.lang.Object data_ = "";
      /**
       * <code>string data = 4;</code>
       */
      public java.lang.String getData() {
        java.lang.Object ref = data_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          data_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string data = 4;</code>
       */
      public com.google.protobuf.ByteString
          getDataBytes() {
        java.lang.Object ref = data_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          data_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string data = 4;</code>
       */
      public Builder setData(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        data_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string data = 4;</code>
       */
      public Builder clearData() {
        
        data_ = getDefaultInstance().getData();
        onChanged();
        return this;
      }
      /**
       * <code>string data = 4;</code>
       */
      public Builder setDataBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        data_ = value;
        onChanged();
        return this;
      }

      private boolean completed_ ;
      /**
       * <code>bool completed = 5;</code>
       */
      public boolean getCompleted() {
        return completed_;
      }
      /**
       * <code>bool completed = 5;</code>
       */
      public Builder setCompleted(boolean value) {
        
        completed_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>bool completed = 5;</code>
       */
      public Builder clearCompleted() {
        
        completed_ = false;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:push.Message)
    }

    // @@protoc_insertion_point(class_scope:push.Message)
    private static final com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message();
    }

    public static com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Message>
        PARSER = new com.google.protobuf.AbstractParser<Message>() {
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Message(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Message> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    public com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_push_Message_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_push_Message_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rMessage.proto\022\004push\"W\n\007Message\022\n\n\002id\030\001" +
      " \001(\003\022\014\n\004type\030\002 \001(\005\022\021\n\ttimestamp\030\003 \001(\003\022\014\n" +
      "\004data\030\004 \001(\t\022\021\n\tcompleted\030\005 \001(\010B7\n\'com.va" +
      "nilla.remoting.spi.codec.protobufB\014Messa" +
      "geProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_push_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_push_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_push_Message_descriptor,
        new java.lang.String[] { "Id", "Type", "Timestamp", "Data", "Completed", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
